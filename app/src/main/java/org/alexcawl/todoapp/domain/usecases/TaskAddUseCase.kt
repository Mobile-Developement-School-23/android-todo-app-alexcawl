package org.alexcawl.todoapp.domain.usecases

import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.service.TaskService
import org.alexcawl.todoapp.domain.util.ValidationException

object TaskAddUseCase {
    private val service: TaskService = TaskService.getInstance()

    @Throws(ValidationException::class)
    suspend operator fun invoke(
        text: String, priority: Priority, deadline: Long?
    ): Unit = service.addTask(text, priority, deadline)
}