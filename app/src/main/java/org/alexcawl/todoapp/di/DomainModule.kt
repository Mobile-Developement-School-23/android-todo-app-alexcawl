package org.alexcawl.todoapp.di

import dagger.Module
import dagger.Provides
import org.alexcawl.todoapp.data.database.datasource.DatabaseSource
import org.alexcawl.todoapp.data.database.usecases_impl.*
import org.alexcawl.todoapp.data.network.datasource.NetworkSource
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.*
import org.alexcawl.todoapp.presentation.util.PreferencesCommitter
import javax.inject.Singleton

@Module
class DomainModule {
    @Provides
    @Singleton
    fun provideTaskGetAllUseCase(repository: TaskRepository): TaskGetAllUseCase =
        TaskGetAllUseCaseImpl(repository)

    @Provides
    @Singleton
    fun provideTaskGetByIdUseCase(repository: TaskRepository): TaskGetByIdUseCase =
        TaskGetByIdUseCaseImpl(repository)

    @Provides
    @Singleton
    fun provideTaskRemoveUseCase(repository: TaskRepository): TaskRemoveUseCase =
        TaskRemoveUseCaseImpl(repository)

    @Provides
    @Singleton
    fun provideTaskUpdateUseCase(repository: TaskRepository): TaskUpdateUseCase =
        TaskUpdateUseCaseImpl(repository)

    @Provides
    @Singleton
    fun provideTaskAddUseCase(repository: TaskRepository): TaskAddUseCase =
        TaskAddUseCaseImpl(repository)

    @Provides
    @Singleton
    fun provideTaskRepository(
        dataSource: DatabaseSource,
        networkSource: NetworkSource,
        committer: PreferencesCommitter
    ): TaskRepository =
        TaskRepository(dataSource, networkSource, committer)
}