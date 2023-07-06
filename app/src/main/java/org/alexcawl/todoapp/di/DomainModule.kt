package org.alexcawl.todoapp.di

import dagger.Binds
import dagger.Module
import org.alexcawl.todoapp.data.repository.TaskRemoteRepositoryImpl
import org.alexcawl.todoapp.data.repository.TaskLocalRepositoryImpl
import org.alexcawl.todoapp.data.usecases.*
import org.alexcawl.todoapp.domain.repository.Synchronizer
import org.alexcawl.todoapp.domain.repository.TaskRemoteRepository
import org.alexcawl.todoapp.domain.repository.TaskLocalRepository
import org.alexcawl.todoapp.domain.usecases.*

@Module
interface DomainModule {
    @Binds
    fun bindTaskGetAllUseCase(useCase: GetTasksUseCaseImpl): GetTasksUseCase

    @Binds
    fun bindTaskGetByIdUseCase(useCase: GetTaskUseCaseImpl): GetTaskUseCase

    @Binds
    fun bindTaskRemoveUseCase(useCase: DeleteTaskUseCaseImpl): DeleteTaskUseCase

    @Binds
    fun bindTaskUpdateUseCase(useCase: UpdateTaskUseCaseImpl): UpdateTaskUseCase

    @Binds
    fun bindTaskAddUseCase(useCase: AddTaskUseCaseImpl): AddTaskUseCase

    @Binds
    fun bindSynchronizeUseCase(useCase: SynchronizeUseCaseImpl): SynchronizeUseCase

    @Binds
    fun bindLocalRepository(repository: TaskLocalRepositoryImpl): TaskLocalRepository

    @Binds
    fun bindRemoteRepository(repository: TaskRemoteRepositoryImpl): TaskRemoteRepository

    @Binds
    fun bindSynchronizer(implementation: TaskRemoteRepositoryImpl): Synchronizer
}