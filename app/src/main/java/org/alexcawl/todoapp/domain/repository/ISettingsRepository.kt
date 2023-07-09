package org.alexcawl.todoapp.domain.repository

interface ISettingsRepository {
    fun getServerEnabled(): Boolean

    fun setServerEnabled(mode: Boolean)

    fun getRevision(): Int

    fun setRevision(revision: Int)

    fun getUsername(): String

    fun setUsername(username: String)

    fun getToken(): String

    fun setToken(token: String)
}