package org.alexcawl.todoapp.domain.usecases

import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.service.TaskService

object TaskEditUseCase {
    private val service: TaskService = TaskService.getInstance()

    suspend operator fun invoke(task: TaskModel): Unit = service.setTask(task)

    suspend operator fun invoke(tasks: List<TaskModel>): Unit = service.setTasks(tasks)
}