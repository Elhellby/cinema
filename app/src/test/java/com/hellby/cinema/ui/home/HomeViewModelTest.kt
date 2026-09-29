package com.hellby.cinema.ui.home

import android.content.Context
import app.cash.turbine.test
import com.hellby.cinema.domain.model.HomeContent
import com.hellby.cinema.domain.usecase.GetHomeContentUseCase
import com.hellby.cinema.util.UiState
import com.hellby.cinema.MainDispatcherRule
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
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context = mockk<Context> {
        every { getString(any()) } returns "Ocurrió un error"
    }
    private val getHomeContentUseCase = mockk<GetHomeContentUseCase>()

    @Test
    fun `estado inicial pasa de Loading a Success`() = runTest {
        val content = HomeContent(featured = emptyList(), sections = emptyList())
        coEvery { getHomeContentUseCase(false) } returns Result.success(content)

        val viewModel = HomeViewModel(context, getHomeContentUseCase)

        viewModel.uiState.test {
            assertEquals(UiState.Success(content), awaitItem())
        }
    }

    @Test
    fun `estado inicial pasa de Loading a Error cuando falla`() = runTest {
        val exception = IOException("sin red")
        coEvery { getHomeContentUseCase(false) } returns Result.failure(exception)

        val viewModel = HomeViewModel(context, getHomeContentUseCase)

        viewModel.uiState.test {
            val error = awaitItem()
            assertTrue(error is UiState.Error)
        }
    }

    @Test
    fun `refresh vuelve a intentar la carga tras un error`() = runTest {
        val content = HomeContent(featured = emptyList(), sections = emptyList())
        coEvery { getHomeContentUseCase(false) } returns Result.failure(IOException("sin red"))
        coEvery { getHomeContentUseCase(true) } returns Result.success(content)

        val viewModel = HomeViewModel(context, getHomeContentUseCase)

        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Error)
            viewModel.refresh()
            assertEquals(UiState.Success(content), expectMostRecentItem())
        }
    }
}
