package org.alexcawl.todoapp.data.network.util

sealed class NetworkState <out T> {
    object Loading : NetworkState<Nothing>()
    data class OK(val revision: Int) : NetworkState<Nothing>()
    data class Success<T>(val data: T, val revision: Int) : NetworkState<T>()
    data class Failure(val cause: Throwable): NetworkState<Nothing>()
}
