package org.alexcawl.todoapp.presentation.util

sealed class UiState<out T> {
    object Start : UiState<Nothing>()
    object OK: UiState<Nothing>()
    data class Success<T>(val data: T, val message: String? = null) : UiState<T>()
    data class Error(val cause: String) : UiState<Nothing>()
}
