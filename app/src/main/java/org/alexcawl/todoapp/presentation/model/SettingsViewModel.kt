package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.domain.usecases.ISettingsOperateUseCase
import org.alexcawl.todoapp.presentation.util.ThemeState

class SettingsViewModel(
    private val settingsUseCase: ISettingsOperateUseCase
): ViewModel() {
    private val _networkEnabled: MutableStateFlow<Boolean> = MutableStateFlow(
        settingsUseCase.getServerEnabled()
    )
    val networkEnabled: StateFlow<Boolean> = _networkEnabled.asStateFlow()

    private val _username: MutableStateFlow<String> = MutableStateFlow(
        settingsUseCase.getUsername()
    )
    val username: StateFlow<String> = _username.asStateFlow()

    private val _token: MutableStateFlow<String> = MutableStateFlow(
        settingsUseCase.getToken()
    )
    val token: StateFlow<String> = _token.asStateFlow()

    fun saveSettings() {
        val settingServer: Boolean = networkEnabled.value
        val settingUsername: String = username.value
        val settingToken: String = token.value
        settingsUseCase.setServerEnabled(settingServer)
        settingsUseCase.setUsername(settingUsername)
        settingsUseCase.setToken(settingToken)
    }

    fun setServerEnabled(mode: Boolean) {
        viewModelScope.launch {
            _networkEnabled.emit(mode)
        }
    }

    fun setUsername(username: String) {
        viewModelScope.launch {
            _username.emit(username)
        }
    }

    fun setToken(token: String) {
        viewModelScope.launch {
            _token.emit(token)
        }
    }

    fun getTheme(): ThemeState = settingsUseCase.getTheme()

    fun setTheme(theme: ThemeState) {
        settingsUseCase.setTheme(theme)
    }
}