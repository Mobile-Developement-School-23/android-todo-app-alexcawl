package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.ITaskLocalRepository
import org.alexcawl.todoapp.domain.usecases.IGetTasksUseCase
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: ITaskLocalRepository
) : IGetTasksUseCase {
    override operator fun invoke(): Flow<DataState<List<TaskModel>>> = repository.getTasks()
}