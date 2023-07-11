package org.alexcawl.todoapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.data.util.ValidationException
import org.alexcawl.todoapp.domain.model.DataState

interface IUpdateTaskUseCase {
    @Throws(ValidationException::class)
    suspend operator fun invoke(task: TaskModel): Flow<DataState<Boolean>>
}