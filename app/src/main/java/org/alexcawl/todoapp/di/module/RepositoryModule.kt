package org.alexcawl.todoapp.di.module

import dagger.Binds
import dagger.Module
import org.alexcawl.todoapp.data.repository.TaskLocalRepository
import org.alexcawl.todoapp.data.repository.TaskRemoteRepository
import org.alexcawl.todoapp.di.scope.ApplicationScope
import org.alexcawl.todoapp.domain.repository.ISynchronizer
import org.alexcawl.todoapp.domain.repository.ITaskLocalRepository
import org.alexcawl.todoapp.domain.repository.ITaskRemoteRepository

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
}