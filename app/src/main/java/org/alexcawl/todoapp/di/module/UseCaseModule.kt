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
    fun bindTaskGetAllUseCase(useCase: GetTasksUseCase): IGetTasksUseCase

    @Binds
    @MainActivityScope
    fun bindTaskGetByIdUseCase(useCase: GetTaskUseCase): IGetTaskUseCase

    @Binds
    @MainActivityScope
    fun bindTaskRemoveUseCase(useCase: DeleteTaskUseCase): IDeleteTaskUseCase

    @Binds
    @MainActivityScope
    fun bindTaskUpdateUseCase(useCase: UpdateTaskUseCase): IUpdateTaskUseCase

    @Binds
    @MainActivityScope
    fun bindTaskAddUseCase(useCase: AddTaskUseCase): IAddTaskUseCase

    @Binds
    @MainActivityScope
    fun bindSynchronizeUseCase(useCase: SyncUseCase): ISyncUseCase
}