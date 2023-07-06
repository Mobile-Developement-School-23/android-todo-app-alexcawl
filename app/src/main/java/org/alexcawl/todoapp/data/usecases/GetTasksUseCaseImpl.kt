package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskLocalRepository
import org.alexcawl.todoapp.domain.usecases.GetTasksUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTasksUseCaseImpl @Inject constructor(
    private val repository: TaskLocalRepository
) : GetTasksUseCase {
    override operator fun invoke(): Flow<DataState<List<TaskModel>>> = repository.getTasks()
}