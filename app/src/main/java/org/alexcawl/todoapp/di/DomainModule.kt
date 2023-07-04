package org.alexcawl.todoapp.di

import dagger.Binds
import dagger.Module
import org.alexcawl.todoapp.data.repository.TaskRepositoryImpl
import org.alexcawl.todoapp.data.usecases.*
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.*

@Module
interface DomainModule {
    @Binds
    fun bindTaskGetAllUseCase(useCase: TaskGetAllUseCaseImpl): TaskGetAllUseCase

    @Binds
    fun bindTaskGetByIdUseCase(useCase: TaskGetByIdUseCaseImpl): TaskGetByIdUseCase

    @Binds
    fun bindTaskRemoveUseCase(useCase: TaskRemoveUseCaseImpl): TaskRemoveUseCase

    @Binds
    fun bindTaskUpdateUseCase(useCase: TaskUpdateUseCaseImpl): TaskUpdateUseCase

    @Binds
    fun bindTaskAddUseCase(useCase: TaskAddUseCaseImpl): TaskAddUseCase

    @Binds
    fun bindTaskRepository(repository: TaskRepositoryImpl): TaskRepository
}