package org.alexcawl.todoapp.domain.repository

interface ICredentialRepository {
    fun getUsername(): String

    fun setUsername(username: String)

    fun getToken(): String

    fun setToken(token: String)
}