package org.alexcawl.todoapp.data.database.usecases_impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskRepository
import org.alexcawl.todoapp.domain.usecases.TaskGetAllUseCase

class TaskGetAllUseCaseImpl(
    private val repository: TaskRepository
) : TaskGetAllUseCase {
    override operator fun invoke(): Flow<DataState<List<TaskModel>>> = repository.getAllTasks().also {
        Log.println(Log.INFO, "NETWORKBRO", "Hi in UseCase")
    }
}