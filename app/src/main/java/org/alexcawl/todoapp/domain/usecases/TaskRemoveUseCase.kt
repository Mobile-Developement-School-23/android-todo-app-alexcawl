package org.alexcawl.todoapp.domain.usecases

import org.alexcawl.todoapp.domain.model.TaskModel

interface TaskRemoveUseCase {
    suspend operator fun invoke(task: TaskModel)
}