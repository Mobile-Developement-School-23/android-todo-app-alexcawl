package org.alexcawl.todoapp.di.module

import dagger.Binds
import dagger.Module
import org.alexcawl.todoapp.data.repository.SettingsRepository
import org.alexcawl.todoapp.data.repository.TaskLocalRepository
import org.alexcawl.todoapp.data.repository.TaskRemoteRepository
import org.alexcawl.todoapp.di.scope.ApplicationScope
import org.alexcawl.todoapp.domain.repository.*

@Module
interface RepositoryModule {
    @Binds
    @ApplicationScope
    fun bindLocalRepository(repository: TaskLocalRepository): ITaskLocalRepository

    @Binds
    @ApplicationScope
    fun bindRemoteRepository(repository: TaskRemoteRepository): ITaskRemoteRepository

    @Binds
    @ApplicationScope
    fun bindSynchronizer(implementation: TaskRemoteRepository): ISynchronizer

    @Binds
    @ApplicationScope
    fun bindSettingsRepository(repository: SettingsRepository): ISettingsRepository

    @Binds
    @ApplicationScope
    fun bindRevisionRepository(repository: SettingsRepository): IRevisionRepository
}