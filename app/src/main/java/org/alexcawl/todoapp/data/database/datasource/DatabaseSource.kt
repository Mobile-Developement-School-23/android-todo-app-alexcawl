package org.alexcawl.todoapp.data.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.alexcawl.todoapp.data.database.dao.TaskDao
import org.alexcawl.todoapp.data.database.entity.TaskEntity
import org.alexcawl.todoapp.data.util.DataState
import org.alexcawl.todoapp.domain.model.TaskModel

class DatabaseSource(
    private val dao: TaskDao
) {
    fun getAllTasks(): Flow<DataState<List<TaskModel>>> = flow {
        emit(DataState.Initial)
        dao.getAllTasks().collect { list ->
            list.also { emit(DataState.Loading) }
                .map { task -> task.toModel() }
                .sortedByDescending { maxOf(it.creationTime, it.modifyingTime ?: 0) }
                .also { tasks -> emit(DataState.OK(tasks)) }
        }
    }

    fun getUncompletedTasks(): Flow<DataState<List<TaskModel>>> = flow {
        emit(DataState.Initial)
        dao.getAllTasks().collect { list ->
            list.also { emit(DataState.Loading) }
                .filter { !it.isDone }
                .map { task -> task.toModel() }
                .sortedByDescending { maxOf(it.creationTime, it.modifyingTime ?: 0) }
                .also { tasks -> emit(DataState.OK(tasks)) }
        }
    }

    fun getTask(id: String): Flow<DataState<TaskModel>> = flow {
        emit(DataState.Initial)
        dao.getTaskById(id).collect {
            when(it) {
                null -> emit(DataState.NotFound)
                else -> emit(DataState.OK(it.toModel()))
            }
        }
    }

    suspend fun updateTask(task: TaskEntity) = dao.addTask(task)

    suspend fun removeTask(task: TaskEntity) = dao.removeTask(task)
}