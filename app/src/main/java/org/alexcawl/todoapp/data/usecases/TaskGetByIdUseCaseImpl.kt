package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskGetByIdUseCase
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskGetByIdUseCaseImpl @Inject constructor(
    private val repository: TaskRepository
) : TaskGetByIdUseCase {
    override operator fun invoke(id: UUID): Flow<DataState<TaskModel>> = repository.getTask(id)
}