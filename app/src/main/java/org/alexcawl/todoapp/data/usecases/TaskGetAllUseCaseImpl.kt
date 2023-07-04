package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskGetAllUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskGetAllUseCaseImpl @Inject constructor(
    private val repository: TaskRepository
) : TaskGetAllUseCase {
    override operator fun invoke(): Flow<DataState<List<TaskModel>>> = repository.getTasks()
}