package com.hellby.cinema.data.remote

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

class TvMazeApiServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var apiService: TvMazeApiService

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
        apiService = retrofit.create(TvMazeApiService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getShows llama al endpoint shows con el query page correcto`() = runBlocking {
        val body = readResource("shows_page0.json")
        server.enqueue(MockResponse().setBody(body).setResponseCode(200))

        apiService.getShows(page = 0)

        val request = server.takeRequest()
        assertEquals("/shows?page=0", request.path)
    }

    @Test
    fun `getShows decodifica una lista con nulos y campos desconocidos`() = runBlocking {
        val body = readResource("shows_page0.json")
        server.enqueue(MockResponse().setBody(body).setResponseCode(200))

        val shows = apiService.getShows(page = 0)

        assertEquals(2, shows.size)
        assertEquals("Under the Dome", shows[0].name)
        assertEquals(6.5, shows[0].rating?.average)
        assertEquals("CBS", shows[0].network?.name)

        assertEquals("Person of Interest", shows[1].name)
        assertNull(shows[1].runtime)
        assertNull(shows[1].network)
        assertEquals("CBS All Access", shows[1].webChannel?.name)
    }

    private fun readResource(name: String): String =
        checkNotNull(javaClass.classLoader?.getResourceAsStream(name)) { "Resource $name not found" }
            .bufferedReader()
            .readText()
}
