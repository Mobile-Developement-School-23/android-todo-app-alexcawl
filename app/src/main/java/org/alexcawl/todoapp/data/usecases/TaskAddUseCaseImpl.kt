package org.alexcawl.todoapp.data.usecases

import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskAddUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskAddUseCaseImpl @Inject constructor(
    private val repository: TaskRepository
) : TaskAddUseCase {
    override suspend fun invoke(text: String, priority: Priority, deadline: Long?) =
        repository.addTask(text, priority, deadline)
}