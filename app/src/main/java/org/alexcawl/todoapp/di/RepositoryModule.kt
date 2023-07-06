package org.alexcawl.todoapp.di

import dagger.Binds
import dagger.Module
import org.alexcawl.todoapp.data.repository.TaskLocalRepositoryImpl
import org.alexcawl.todoapp.data.repository.TaskRemoteRepositoryImpl
import org.alexcawl.todoapp.domain.repository.Synchronizer
import org.alexcawl.todoapp.domain.repository.TaskLocalRepository
import org.alexcawl.todoapp.domain.repository.TaskRemoteRepository

@Module
interface RepositoryModule {
    @Binds
    @MainActivityScope
    fun bindLocalRepository(repository: TaskLocalRepositoryImpl): TaskLocalRepository

    @Binds
    @MainActivityScope
    fun bindRemoteRepository(repository: TaskRemoteRepositoryImpl): TaskRemoteRepository

    @Binds
    @MainActivityScope
    fun bindSynchronizer(implementation: TaskRemoteRepositoryImpl): Synchronizer
}