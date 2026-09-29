package com.hellby.cinema.ui.detail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellby.cinema.domain.model.ShowDetail
import com.hellby.cinema.domain.usecase.GetShowDetailUseCase
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
class DetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val getShowDetailUseCase: GetShowDetailUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ShowDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<ShowDetail>> = _uiState.asStateFlow()

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        val showId = savedStateHandle.get<Int>("showId") ?: 0
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = getShowDetailUseCase(showId)
            result.fold(
                onSuccess = { _uiState.value = UiState.Success(it) },
                onFailure = { _uiState.value = UiState.Error(context.getString(it.toUserMessageRes()), it) }
            )
        }
    }
}
