package org.alexcawl.todoapp.data.util

sealed class DataState<out T> {
    object Initial : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    object NotFound : DataState<Nothing>()
    data class OK<T>(val content: T) : DataState<T>()
}
