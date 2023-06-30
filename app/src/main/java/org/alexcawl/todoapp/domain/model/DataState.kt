package org.alexcawl.todoapp.domain.model

sealed class DataState<out T> {
    object Initial : DataState<Nothing>()
    data class Deprecated<T>(val data: T) : DataState<T>()
    data class Latest<T>(val data: T) : DataState<T>()
    data class Exception(val cause: Throwable): DataState<Nothing>()
}
