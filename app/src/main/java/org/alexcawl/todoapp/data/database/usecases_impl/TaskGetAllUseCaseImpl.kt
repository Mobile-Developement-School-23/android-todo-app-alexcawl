package org.alexcawl.todoapp.data.database.usecases_impl

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.data.util.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.service.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskGetAllUseCase

class TaskGetAllUseCaseImpl(
    private val repository: TaskRepository
) : TaskGetAllUseCase {
    override operator fun invoke(): Flow<DataState<List<TaskModel>>> = repository.getAllTasks()
}