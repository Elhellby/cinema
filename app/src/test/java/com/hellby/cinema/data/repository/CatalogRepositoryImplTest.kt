package com.hellby.cinema.data.repository

import com.hellby.cinema.data.remote.ImageDto
import com.hellby.cinema.data.remote.RatingDto
import com.hellby.cinema.data.remote.ShowDto
import com.hellby.cinema.data.remote.TvMazeRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CatalogRepositoryImplTest {

    private val remoteDataSource = mockk<TvMazeRemoteDataSource>()
    private val dispatcher = UnconfinedTestDispatcher()
    private val repository = CatalogRepositoryImpl(remoteDataSource, dispatcher)

    private fun showDto(
        id: Int,
        name: String,
        rating: Double? = null,
        premiered: String? = null,
        weight: Int? = null,
        genres: List<String> = emptyList(),
        hasImage: Boolean = true
    ) = ShowDto(
        id = id,
        name = name,
        rating = RatingDto(average = rating),
        premiered = premiered,
        weight = weight,
        genres = genres,
        image = if (hasImage) ImageDto(medium = "//img/$id.jpg") else null
    )

    @Test
    fun `getHomeContent arma el carrusel con top 8 por rating y descarta shows sin imagen`() = runTest {
        val shows = (1..10).map { showDto(it, "Show $it", rating = it.toDouble(), hasImage = it != 10) }
        coEvery { remoteDataSource.getShows(0) } returns Result.success(shows)

        val result = repository.getHomeContent()

        val content = result.getOrThrow()
        assertEquals(8, content.featured.size)
        assertTrue(content.featured.none { it.id == 10 })
        assertEquals(listOf(9, 8, 7, 6, 5, 4, 3, 2), content.featured.map { it.id })
    }

    @Test
    fun `getHomeContent descarta secciones vacias`() = runTest {
        val shows = listOf(
            showDto(1, "Solo drama", genres = listOf("Drama"))
        )
        coEvery { remoteDataSource.getShows(0) } returns Result.success(shows)

        val result = repository.getHomeContent()

        val sectionTitles = result.getOrThrow().sections.map { it.title }
        assertTrue("Comedia" !in sectionTitles)
    }

    @Test
    fun `getHomeContent ordena Populares por weight descendente`() = runTest {
        val shows = listOf(
            showDto(1, "Bajo", weight = 10),
            showDto(2, "Alto", weight = 90),
            showDto(3, "Medio", weight = 50)
        )
        coEvery { remoteDataSource.getShows(0) } returns Result.success(shows)

        val result = repository.getHomeContent()

        val popular = result.getOrThrow().sections.first { it.title == "Populares" }
        assertEquals(listOf(2, 3, 1), popular.shows.map { it.id })
    }

    @Test
    fun `getHomeContent usa cache en la segunda llamada sin golpear la red`() = runTest {
        val shows = listOf(showDto(1, "Show", rating = 8.0))
        coEvery { remoteDataSource.getShows(0) } returns Result.success(shows)

        repository.getHomeContent()
        repository.getHomeContent()

        coVerify(exactly = 1) { remoteDataSource.getShows(0) }
    }

    @Test
    fun `getHomeContent con forceRefresh ignora la cache y golpea la red de nuevo`() = runTest {
        val shows = listOf(showDto(1, "Show", rating = 8.0))
        coEvery { remoteDataSource.getShows(0) } returns Result.success(shows)

        repository.getHomeContent()
        repository.getHomeContent(forceRefresh = true)

        coVerify(exactly = 2) { remoteDataSource.getShows(0) }
    }

    @Test
    fun `getHomeContent propaga el error del remote data source`() = runTest {
        val exception = IOException("network down")
        coEvery { remoteDataSource.getShows(0) } returns Result.failure(exception)

        val result = repository.getHomeContent()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
