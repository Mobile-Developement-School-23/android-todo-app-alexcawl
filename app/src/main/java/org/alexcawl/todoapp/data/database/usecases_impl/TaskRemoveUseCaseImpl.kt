package org.alexcawl.todoapp.data.database.usecases_impl

import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskRemoveUseCase

class TaskRemoveUseCaseImpl(
    private val repository: TaskRepository
) : TaskRemoveUseCase {
    override suspend operator fun invoke(task: TaskModel) =
        repository.removeTask(task)
}