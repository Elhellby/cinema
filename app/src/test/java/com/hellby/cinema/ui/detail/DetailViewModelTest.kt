package com.hellby.cinema.ui.detail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.hellby.cinema.MainDispatcherRule
import com.hellby.cinema.domain.model.CastMember
import com.hellby.cinema.domain.model.Show
import com.hellby.cinema.domain.model.ShowDetail
import com.hellby.cinema.domain.usecase.GetShowDetailUseCase
import com.hellby.cinema.util.UiState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context = mockk<Context> {
        every { getString(any()) } returns "Ocurrió un error"
    }
    private val getShowDetailUseCase = mockk<GetShowDetailUseCase>()

    private fun fakeDetail() = ShowDetail(
        show = Show(
            id = 1,
            title = "Show",
            shortDescription = "desc",
            fullDescription = "desc completa",
            imageUrl = null,
            posterUrl = null,
            genres = emptyList(),
            rating = 8.0,
            premiered = "2020-01-01",
            status = "Running",
            runtime = 30,
            language = "English",
            network = "HBO"
        ),
        cast = listOf(CastMember(id = 1, name = "Actor", character = "Personaje", imageUrl = null)),
        officialSite = null
    )

    @Test
    fun `carga inicial pasa de Loading a Success con el showId del SavedStateHandle`() = runTest {
        val detail = fakeDetail()
        coEvery { getShowDetailUseCase(1) } returns Result.success(detail)
        val savedStateHandle = SavedStateHandle(mapOf("showId" to 1))

        val viewModel = DetailViewModel(context, savedStateHandle, getShowDetailUseCase)

        viewModel.uiState.test {
            assertEquals(UiState.Success(detail), awaitItem())
        }
    }

    @Test
    fun `carga inicial pasa de Loading a Error cuando falla`() = runTest {
        coEvery { getShowDetailUseCase(1) } returns Result.failure(IOException("sin red"))
        val savedStateHandle = SavedStateHandle(mapOf("showId" to 1))

        val viewModel = DetailViewModel(context, savedStateHandle, getShowDetailUseCase)

        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Error)
        }
    }

    @Test
    fun `retry vuelve a intentar la carga tras un error`() = runTest {
        val detail = fakeDetail()
        coEvery { getShowDetailUseCase(1) } returnsMany listOf(
            Result.failure(IOException("sin red")),
            Result.success(detail)
        )
        val savedStateHandle = SavedStateHandle(mapOf("showId" to 1))

        val viewModel = DetailViewModel(context, savedStateHandle, getShowDetailUseCase)

        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Error)
            viewModel.retry()
            assertEquals(UiState.Success(detail), expectMostRecentItem())
        }
    }
}
