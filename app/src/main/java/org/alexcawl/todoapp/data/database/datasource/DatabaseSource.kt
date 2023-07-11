package org.alexcawl.todoapp.data.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.alexcawl.todoapp.data.database.dao.TaskDao
import org.alexcawl.todoapp.data.database.entity.TaskEntity
import org.alexcawl.todoapp.data.database.util.RoomState
import org.alexcawl.todoapp.data.database.util.DatabaseException
import org.alexcawl.todoapp.data.util.toEntity
import org.alexcawl.todoapp.data.util.toModel
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.source.IDatabaseSource
import java.util.*
import javax.inject.Inject

/**
 * Room data source
 * @param dao Room DAO
 * @see TaskDao
 * */
class DatabaseSource @Inject constructor(
    private val dao: TaskDao
): IDatabaseSource {
    override fun getTasks(): Flow<RoomState<List<TaskModel>>> = flow {
        emit(RoomState.Initial)
        dao.getTasks().collect { list ->
            list.map(TaskEntity::toModel)
                .sortedByDescending { maxOf(it.creationTime, it.modifyingTime) }
                .also { tasks -> emit(RoomState.Success(tasks)) }
        }
    }.catch {
        emit(RoomState.Failure(it))
    }

    override fun getTask(uuid: UUID): Flow<RoomState<TaskModel>> = flow {
        emit(RoomState.Initial)
        dao.getTask(uuid.toString()).collect {
            when (it) {
                null -> emit(RoomState.Failure(DatabaseException("Item not found!")))
                else -> emit(RoomState.Success(it.toModel()))
            }
        }
    }.catch {
        emit(RoomState.Failure(it))
    }

    override suspend fun addTask(task: TaskModel) = dao.addTask(task.toEntity())

    override suspend fun deleteTask(task: TaskModel) = dao.removeTask(task.toEntity())

    override suspend fun getTasksAsList(): List<TaskModel> = dao.getTasksAsEntity().map(TaskEntity::toModel)

    override suspend fun getTaskAsEntity(uuid: UUID): TaskModel? = dao.getTaskAsEntity(uuid.toString())?.toModel()

    override fun overwrite(tasks: List<TaskModel>) {
        dao.removeTasks()
        dao.addTasks(tasks.map(TaskModel::toEntity))
    }
}