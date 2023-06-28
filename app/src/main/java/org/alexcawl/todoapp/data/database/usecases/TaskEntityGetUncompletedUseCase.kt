package org.alexcawl.todoapp.data.database.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.alexcawl.todoapp.data.database.db.DatabaseModule
import org.alexcawl.todoapp.data.database.repository.TaskRepository
import org.alexcawl.todoapp.domain.model.TaskModel

object TaskEntityGetUncompletedUseCase {
    private val repository = TaskRepository(DatabaseModule.appDatabase.taskDao())

    operator fun invoke(): Flow<List<TaskModel>> = repository.getAllUncompletedTasks().map { list ->
        list.map { entity -> entity.toModel() }
    }
}