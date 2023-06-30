package org.alexcawl.todoapp.data.database.usecases_impl

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskGetByIdUseCase
import java.util.*

class TaskGetByIdUseCaseImpl(
    private val repository: TaskRepository
) : TaskGetByIdUseCase {
    override operator fun invoke(id: UUID): Flow<DataState<TaskModel>> = repository.getTaskById(id)
}