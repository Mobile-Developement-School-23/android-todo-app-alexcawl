package org.alexcawl.todoapp.data.usecases

import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskRemoveUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRemoveUseCaseImpl @Inject constructor(
    private val repository: TaskRepository
) : TaskRemoveUseCase {
    override suspend operator fun invoke(task: TaskModel) =
        repository.deleteTask(task)
}