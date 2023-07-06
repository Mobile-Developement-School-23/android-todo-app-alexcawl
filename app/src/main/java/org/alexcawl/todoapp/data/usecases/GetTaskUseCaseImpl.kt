package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskLocalRepository
import org.alexcawl.todoapp.domain.usecases.GetTaskUseCase
import java.util.*
import javax.inject.Inject

class GetTaskUseCaseImpl @Inject constructor(
    private val repository: TaskLocalRepository
) : GetTaskUseCase {
    override operator fun invoke(id: UUID): Flow<DataState<TaskModel>> = repository.getTask(id)
}