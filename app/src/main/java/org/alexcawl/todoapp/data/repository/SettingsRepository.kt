package org.alexcawl.todoapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import org.alexcawl.todoapp.domain.repository.ISettingsRepository
import org.alexcawl.todoapp.presentation.util.ThemeState
import javax.inject.Inject

/**
 * Shared preferneces data source
 * @param context Application context
 * @see SharedPreferences
 * */
class SettingsRepository @Inject constructor(
    context: Context
): ISettingsRepository {
    private companion object {
        const val NAME: String = "ToDoPrefs"

        const val SERVER_ENABLED: String = "SERVER_ENABLED"
        const val SERVER_ENABLED_DEFAULT: Boolean = false

        const val REVISION: String = "REVISION"
        const val REVISION_DEFAULT: Int = 0

        const val USERNAME: String = "USERNAME"
        const val USERNAME_DEFAULT: String = ""

        const val TOKEN: String = "TOKEN"
        const val TOKEN_DEFAULT: String = ""

        const val THEME: String = "THEME"
        const val THEME_DEFAULT: String = "DEFAULT"

        const val NOTIFICATION_ENABLED: String = "NOTIFICATION_ENABLED"
        const val NOTIFICATION_ENABLED_DEFAULT: Boolean = false
    }

    private val source: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    override fun getServerEnabled(): Boolean = source.getBoolean(SERVER_ENABLED, SERVER_ENABLED_DEFAULT)

    override fun setServerEnabled(mode: Boolean) {
        source.edit().putBoolean(SERVER_ENABLED, mode).apply()
    }

    override fun getRevision(): Int = source.getInt(REVISION, REVISION_DEFAULT)

    override fun setRevision(revision: Int) {
        source.edit().putInt(REVISION, revision).apply()
    }

    override fun getUsername(): String = source.getString(USERNAME, USERNAME_DEFAULT) ?: USERNAME_DEFAULT

    override fun setUsername(username: String) {
        source.edit().putString(USERNAME, username).apply()
    }

    override fun getToken(): String = source.getString(TOKEN, TOKEN_DEFAULT) ?: TOKEN_DEFAULT

    override fun setToken(token: String) {
        source.edit().putString(TOKEN, token).apply()
    }

    override fun getTheme(): ThemeState {
        return try {
            ThemeState.valueOf(source.getString(THEME, THEME_DEFAULT) ?: THEME_DEFAULT)
        } catch (exception: IllegalArgumentException) {
            ThemeState.DEFAULT
        }
    }

    override fun setTheme(theme: ThemeState) {
        source.edit().putString(THEME, theme.toString()).apply()
    }

    override fun getNotificationEnabled(): Boolean = source.getBoolean(NOTIFICATION_ENABLED, NOTIFICATION_ENABLED_DEFAULT)

    override fun setNotificationEnabled(mode: Boolean) {
        source.edit().putBoolean(NOTIFICATION_ENABLED, mode).apply()
    }
}