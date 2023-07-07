package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.ITaskLocalRepository
import org.alexcawl.todoapp.domain.usecases.IGetTaskUseCase
import java.util.*
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val repository: ITaskLocalRepository
) : IGetTaskUseCase {
    override operator fun invoke(id: UUID): Flow<DataState<TaskModel>> = repository.getTask(id)
}