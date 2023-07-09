package org.alexcawl.todoapp.data.usecases

import org.alexcawl.todoapp.domain.repository.ISettingsRepository
import org.alexcawl.todoapp.domain.usecases.ISettingsOperateUseCase
import javax.inject.Inject

class SettingsOperateUseCase @Inject constructor(
    private val repository: ISettingsRepository
): ISettingsOperateUseCase {
    override fun getServerEnabled(): Boolean = repository.getServerEnabled()

    override fun setServerEnabled(mode: Boolean) = repository.setServerEnabled(mode)

    override fun getUsername(): String = repository.getUsername()

    override fun setUsername(username: String) = repository.setUsername(username)

    override fun getToken(): String = repository.getToken()

    override fun setToken(token: String) = repository.setToken(token)
}