package org.alexcawl.todoapp.data.database.usecases_impl

import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskAddUseCase

class TaskAddUseCaseImpl(
    private val repository: TaskRepository
) : TaskAddUseCase {
    override suspend fun invoke(text: String, priority: Priority, deadline: Long?) =
        repository.addTask(text, priority, deadline)
}