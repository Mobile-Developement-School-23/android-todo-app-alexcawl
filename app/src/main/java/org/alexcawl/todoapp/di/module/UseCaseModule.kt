package org.alexcawl.todoapp.di.module

import dagger.Binds
import dagger.Module
import org.alexcawl.todoapp.data.usecases.*
import org.alexcawl.todoapp.di.scope.MainActivityScope
import org.alexcawl.todoapp.domain.usecases.*

@Module
interface UseCaseModule {
    @Binds
    @MainActivityScope
    fun bindTaskGetAllUseCase(useCase: GetTasksUseCaseImpl): GetTasksUseCase

    @Binds
    @MainActivityScope
    fun bindTaskGetByIdUseCase(useCase: GetTaskUseCaseImpl): GetTaskUseCase

    @Binds
    @MainActivityScope
    fun bindTaskRemoveUseCase(useCase: DeleteTaskUseCaseImpl): DeleteTaskUseCase

    @Binds
    @MainActivityScope
    fun bindTaskUpdateUseCase(useCase: UpdateTaskUseCaseImpl): UpdateTaskUseCase

    @Binds
    @MainActivityScope
    fun bindTaskAddUseCase(useCase: AddTaskUseCaseImpl): AddTaskUseCase

    @Binds
    @MainActivityScope
    fun bindSynchronizeUseCase(useCase: SynchronizeUseCaseImpl): SynchronizeUseCase
}