package org.alexcawl.todoapp.data.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.alexcawl.todoapp.data.database.dao.TaskDao
import org.alexcawl.todoapp.data.database.entity.TaskEntity
import org.alexcawl.todoapp.data.database.util.RoomState
import org.alexcawl.todoapp.data.util.DatabaseException
import org.alexcawl.todoapp.data.util.toEntity
import org.alexcawl.todoapp.data.util.toModel
import org.alexcawl.todoapp.domain.model.TaskModel
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSource @Inject constructor(
    private val dao: TaskDao
) {
    fun getTasks(): Flow<RoomState<List<TaskModel>>> = flow {
        emit(RoomState.Initial)
        dao.getTasks().collect { list ->
            list.map(TaskEntity::toModel)
                .sortedByDescending { maxOf(it.creationTime, it.modifyingTime) }
                .also { tasks -> emit(RoomState.Success(tasks)) }
        }
    }.catch {
        emit(RoomState.Failure(it))
    }

    fun getTask(id: UUID): Flow<RoomState<TaskModel>> = flow {
        emit(RoomState.Initial)
        dao.getTask(id.toString()).collect {
            when (it) {
                null -> emit(RoomState.Failure(DatabaseException("Item not found!")))
                else -> emit(RoomState.Success(it.toModel()))
            }
        }
    }.catch {
        emit(RoomState.Failure(it))
    }

    suspend fun updateTask(task: TaskModel) = dao.updateTask(task.toEntity())

    suspend fun deleteTask(task: TaskModel) = dao.removeTask(task.toEntity())

    fun getTasksAsList(): List<TaskModel> = dao.getTasksAsList().map(TaskEntity::toModel)

    fun overwriteDatabase(list: List<TaskModel>) {
        dao.removeTasks()
        dao.updateTasks(list.map(TaskModel::toEntity))
    }
}