package org.alexcawl.todoapp.domain.usecases

import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.service.TaskService

object TaskAddUseCase {
    private val service: TaskService = TaskService.getInstance()

    suspend operator fun invoke(
        text: String, priority: TaskModel.Companion.Priority, deadline: Long?
    ): Unit = service.addTask(text, priority, deadline)
}