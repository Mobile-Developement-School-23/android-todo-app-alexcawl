package org.alexcawl.todoapp.domain.usecases

import org.alexcawl.todoapp.presentation.util.ThemeState

interface ISettingsOperateUseCase {
    fun getServerEnabled(): Boolean

    fun setServerEnabled(mode: Boolean)

    fun getUsername(): String

    fun setUsername(username: String)

    fun getToken(): String

    fun setToken(token: String)

    fun getTheme(): ThemeState

    fun setTheme(theme: ThemeState)
}