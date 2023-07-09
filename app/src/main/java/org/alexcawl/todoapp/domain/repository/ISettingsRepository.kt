package org.alexcawl.todoapp.domain.repository

interface ISettingsRepository : IRevisionRepository, ICredentialRepository {
    fun getVisibility(): Boolean

    fun setVisibility(mode: Boolean)
}