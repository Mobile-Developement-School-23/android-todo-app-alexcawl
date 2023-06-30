package org.alexcawl.todoapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel

interface TaskGetAllUseCase {
    operator fun invoke(): Flow<DataState<List<TaskModel>>>
}