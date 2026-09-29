package com.hellby.cinema.util

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error<T>(val message: String, val cause: Throwable? = null) : UiState<T>
}
