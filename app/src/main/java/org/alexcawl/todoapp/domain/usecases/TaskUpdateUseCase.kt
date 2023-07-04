package org.alexcawl.todoapp.domain.usecases

import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.data.util.ValidationException

interface TaskUpdateUseCase {
    @Throws(ValidationException::class)
    suspend operator fun invoke(task: TaskModel)
}