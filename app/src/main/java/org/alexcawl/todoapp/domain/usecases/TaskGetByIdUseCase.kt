package org.alexcawl.todoapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import java.util.*

interface TaskGetByIdUseCase {
    operator fun invoke(id: UUID): Flow<DataState<TaskModel>>
}