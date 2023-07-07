package org.alexcawl.todoapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel

interface IDeleteTaskUseCase {
    suspend operator fun invoke(task: TaskModel): Flow<DataState<Boolean>>
}