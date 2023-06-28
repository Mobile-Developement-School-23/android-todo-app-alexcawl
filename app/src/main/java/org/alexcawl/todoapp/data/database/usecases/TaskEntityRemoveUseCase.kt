package org.alexcawl.todoapp.data.database.usecases

import org.alexcawl.todoapp.data.database.db.DatabaseModule
import org.alexcawl.todoapp.data.database.repository.TaskRepository
import org.alexcawl.todoapp.domain.model.TaskModel

object TaskEntityRemoveUseCase {
    private val repository = TaskRepository(DatabaseModule.appDatabase.taskDao())

    suspend operator fun invoke(task: TaskModel) = repository.removeTask(task.toEntity())
}