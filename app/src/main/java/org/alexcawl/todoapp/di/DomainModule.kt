package org.alexcawl.todoapp.di

import dagger.Binds
import dagger.Module
import org.alexcawl.todoapp.data.service.SynchronizeServiceImpl
import org.alexcawl.todoapp.data.service.TaskServiceImpl
import org.alexcawl.todoapp.data.usecases.*
import org.alexcawl.todoapp.domain.service.SynchronizeService
import org.alexcawl.todoapp.domain.service.TaskService
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
    fun bindTaskService(service: TaskServiceImpl): TaskService

    @Binds
    fun bindSyncService(service: SynchronizeServiceImpl): SynchronizeService
}