package org.alexcawl.todoapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import org.alexcawl.todoapp.domain.repository.ISettingsRepository
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
        const val NAME: String = "ToDoPref"

        const val SERVER_ENABLED: String = "SERVER_ENABLED"
        const val SERVER_ENABLED_DEFAULT: Boolean = false

        const val REVISION: String = "REVISION"
        const val REVISION_DEFAULT: Int = 0

        const val VISIBILITY: String = "VISIBILITY"
        const val VISIBILITY_DEFAULT: Boolean = true

        const val USERNAME: String = "USERNAME"
        const val USERNAME_DEFAULT: String = ""

        const val TOKEN: String = "TOKEN"
        const val TOKEN_DEFAULT: String = ""
    }

    private val source: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    override fun getVisibility(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setVisibility(mode: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getRevision(): Int = source.getInt(REVISION, REVISION_DEFAULT)

    override fun setRevision(revision: Int) {
        source.edit().putInt(REVISION, revision).apply()
    }

    override fun getUsername(): String {
        TODO("Not yet implemented")
    }

    override fun setUsername(username: String) {
        TODO("Not yet implemented")
    }

    override fun getToken(): String {
        TODO("Not yet implemented")
    }

    override fun setToken(token: String) {
        TODO("Not yet implemented")
    }
}