package com.hellby.cinema.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellby.cinema.domain.model.HomeContent
import com.hellby.cinema.domain.usecase.GetHomeContentUseCase
import com.hellby.cinema.util.UiState
import com.hellby.cinema.util.toUserMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getHomeContentUseCase: GetHomeContentUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<HomeContent>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeContent>> = _uiState.asStateFlow()

    init {
        load(forceRefresh = false)
    }

    fun refresh() = load(forceRefresh = true)

    private fun load(forceRefresh: Boolean) {
        viewModelScope.launch {
            val previousSuccess = _uiState.value is UiState.Success
            if (!previousSuccess || forceRefresh) {
                _uiState.value = UiState.Loading
            }

            val result = getHomeContentUseCase(forceRefresh)
            result.fold(
                onSuccess = { _uiState.value = UiState.Success(it) },
                onFailure = { _uiState.value = UiState.Error(context.getString(it.toUserMessageRes()), it) }
            )
        }
    }
}
