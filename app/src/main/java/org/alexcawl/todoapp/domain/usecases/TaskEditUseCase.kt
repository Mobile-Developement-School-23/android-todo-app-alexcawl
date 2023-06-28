package org.alexcawl.todoapp.domain.usecases

import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.service.TaskService
import org.alexcawl.todoapp.domain.util.ValidationException

object TaskEditUseCase {
    private val service: TaskService = TaskService.getInstance()

    @Throws(ValidationException::class)
    suspend operator fun invoke(task: TaskModel): Unit = service.setTask(task)
}