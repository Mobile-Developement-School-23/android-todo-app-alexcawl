package org.alexcawl.todoapp.data.database.usecases_impl

import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskUpdateUseCase

class TaskUpdateUseCaseImpl(
    private val repository: TaskRepository
) : TaskUpdateUseCase {
    override suspend operator fun invoke(task: TaskModel) =
        repository.updateTask(task)
}