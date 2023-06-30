package org.alexcawl.todoapp.data.database.usecases_impl

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskGetUncompletedUseCase

class TaskGetUncompletedUseCaseImpl(
    private val repository: TaskRepository
) : TaskGetUncompletedUseCase {
    override operator fun invoke(): Flow<DataState<List<TaskModel>>> =
        repository.getUncompletedTasks()
}