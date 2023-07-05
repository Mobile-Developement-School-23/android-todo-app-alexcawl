package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.service.TaskService
import org.alexcawl.todoapp.domain.usecases.GetTaskUseCase
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTaskUseCaseImpl @Inject constructor(
    private val repository: TaskService
) : GetTaskUseCase {
    override operator fun invoke(id: UUID): Flow<DataState<TaskModel>> = repository.getTask(id)
}