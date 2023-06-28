package org.alexcawl.todoapp.data.database.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.alexcawl.todoapp.data.database.db.DatabaseModule
import org.alexcawl.todoapp.data.database.repository.TaskRepository
import org.alexcawl.todoapp.domain.model.TaskModel
import java.util.UUID

object TaskEntityGetByIdUseCase {
    private val repository = TaskRepository(DatabaseModule.appDatabase.taskDao())

    operator fun invoke(id: UUID): Flow<Result<TaskModel>> = repository.getTask(id.toString()).map {
        try {
            Result.success(it!!.toModel())
        } catch (exception: NullPointerException) {
            Result.failure(exception)
        }
    }
}