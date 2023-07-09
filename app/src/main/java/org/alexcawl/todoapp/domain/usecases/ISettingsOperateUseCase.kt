package org.alexcawl.todoapp.domain.usecases

interface ISettingsOperateUseCase {
    fun getServerEnabled(): Boolean

    fun setServerEnabled(mode: Boolean)

    fun getUsername(): String

    fun setUsername(username: String)

    fun getToken(): String

    fun setToken(token: String)
}