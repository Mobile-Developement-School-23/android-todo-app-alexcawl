package org.alexcawl.todoapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.service.TaskService

object TaskGetUseCase {
    private val service: TaskService = TaskService.getInstance()

    operator fun invoke(): Flow<List<TaskModel>> = service.tasks
}