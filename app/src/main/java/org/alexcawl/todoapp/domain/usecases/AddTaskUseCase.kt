package org.alexcawl.todoapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority

interface AddTaskUseCase {
    suspend operator fun invoke(text: String, priority: Priority, deadline: Long?): Flow<DataState<Boolean>>
}