package org.alexcawl.todoapp.domain.usecases

import org.alexcawl.todoapp.domain.model.Priority

interface TaskAddUseCase {
    suspend operator fun invoke(text: String, priority: Priority, deadline: Long?)
}