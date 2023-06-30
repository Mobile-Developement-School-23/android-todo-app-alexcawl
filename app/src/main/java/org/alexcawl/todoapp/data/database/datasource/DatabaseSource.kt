package org.alexcawl.todoapp.data.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.alexcawl.todoapp.data.database.dao.TaskDao
import org.alexcawl.todoapp.data.database.entity.TaskEntity
import org.alexcawl.todoapp.data.database.util.RoomState
import org.alexcawl.todoapp.data.util.toEntity
import org.alexcawl.todoapp.data.util.toModel
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.util.ValidationException
import org.alexcawl.todoapp.presentation.util.PreferencesCommitter
import java.util.*

class DatabaseSource(
    private val committer: PreferencesCommitter,
    private val dao: TaskDao
) {
    fun getTask(id: UUID): Flow<RoomState<TaskModel>> = flow {
        emit(RoomState.Initial)
        dao.getTask(id.toString()).collect {
            when (it) {
                null -> emit(RoomState.Failure(ValidationException("Item not found!")))
                else -> emit(RoomState.Success(it.toModel(), committer.getRevision()))
            }
        }
    }.catch {
        emit(RoomState.Failure(it))
    }

    suspend fun updateTask(task: TaskEntity) = dao.updateTask(task)

    suspend fun removeTask(task: TaskEntity) = dao.removeTask(task)

    fun getTasks(): Flow<RoomState<List<TaskModel>>> = flow {
        emit(RoomState.Initial)
        dao.getTasks().collect { list ->
            list.map(TaskEntity::toModel)
                .sortedByDescending { maxOf(it.creationTime, it.modifyingTime ?: 0) }
                .also { tasks -> emit(RoomState.Success(tasks, committer.getRevision())) }
        }
    }.catch {
        emit(RoomState.Failure(it))
    }

    suspend fun upsertTasks(list: List<TaskModel>, revision: Int) {
        dao.removeTasks()
        dao.updateTasks(list.map(TaskModel::toEntity))
        committer.setRevision(revision)
    }
}