package org.alexcawl.todoapp.data.usecases

import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskUpdateUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskUpdateUseCaseImpl @Inject constructor(
    private val repository: TaskRepository
) : TaskUpdateUseCase {
    override suspend operator fun invoke(task: TaskModel) =
        repository.updateTask(task)
}