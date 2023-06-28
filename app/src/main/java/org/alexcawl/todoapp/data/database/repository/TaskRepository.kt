package org.alexcawl.todoapp.data.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.alexcawl.todoapp.data.database.dao.TaskDao
import org.alexcawl.todoapp.data.database.entity.TaskEntity

class TaskRepository(
    private val dao: TaskDao
) {
    fun getAllTasks(): Flow<List<TaskEntity>> = dao.getAllTasks().map { list ->
        list.toMutableList().sortedByDescending { entity ->
            maxOf(entity.creationTime, entity.modifyingTime ?: 0)
        }
    }

    fun getAllUncompletedTasks(): Flow<List<TaskEntity>> = this.getAllTasks().map { list ->
        list.filter { !it.isDone }
    }

    fun getTask(id: String): Flow<TaskEntity?> = dao.getTaskById(id)

    suspend fun addTask(task: TaskEntity) = dao.addTask(task)

    suspend fun removeTask(task: TaskEntity) = dao.removeTask(task)
}