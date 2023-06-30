package org.alexcawl.todoapp.domain.repository

import kotlinx.coroutines.flow.*
import org.alexcawl.todoapp.data.database.datasource.DatabaseSource
import org.alexcawl.todoapp.data.database.usecases_impl.*
import org.alexcawl.todoapp.data.database.util.RoomState
import org.alexcawl.todoapp.data.network.datasource.NetworkSource
import org.alexcawl.todoapp.data.network.util.NetworkState
import org.alexcawl.todoapp.data.util.toEntity
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.util.ValidationException
import org.alexcawl.todoapp.presentation.util.PreferencesCommitter
import java.util.*

class TaskRepository(
    private val databaseSource: DatabaseSource,
    private val networkSource: NetworkSource,
    private val committer: PreferencesCommitter
) {
    fun getAllTasks(): Flow<DataState<List<TaskModel>>> = flow {
        emit(DataState.Initial)
        databaseSource.getTasks().collect { roomState ->
            when (roomState) {
                is RoomState.Success -> emit(DataState.Result(roomState.data)).also {
                    synchronizeTasks(roomState.revision, roomState.data)
                }
                is RoomState.Failure -> emit(DataState.Exception(roomState.cause))
                else -> {}
            }
        }
    }

    private suspend fun synchronizeTasks(androidRevision: Int, tasks: List<TaskModel>) {
        networkSource.getTasks().collect { networkState ->
            when (networkState) {
                is NetworkState.Success -> {
                    when {
                        tasks != networkState.data || androidRevision != networkState.revision -> networkSource.patchTasks(tasks)
                            .collect {
                                when (it) {
                                    is NetworkState.Success -> committer.setRevision(it.revision)
                                    else -> {}
                                }
                            }
                        else -> {}
                    }
                }
                else -> {}
            }
        }
    }

    fun getTaskById(id: UUID): Flow<DataState<TaskModel>> = flow {
        emit(DataState.Initial)
        databaseSource.getTask(id).collect { roomState ->
            when (roomState) {
                is RoomState.Success -> emit(DataState.Result(roomState.data))
                is RoomState.Failure -> emit(DataState.Exception(roomState.cause))
                else -> {}
            }
        }
    }.catch {
        emit(DataState.Exception(it))
    }

    @Throws(ValidationException::class)
    suspend fun addTask(
        text: String, priority: Priority, deadline: Long?
    ) {
        validateTask(text, deadline)
        val task = buildTaskModel(text, priority, deadline)
        databaseSource.updateTask(buildTaskModel(text, priority, deadline).toEntity())
        networkSource.postTask(task).collect { networkState ->
            when (networkState) {
                is NetworkState.Failure -> committer.updateRevision()
                else -> {}
            }
        }
    }

    suspend fun removeTask(task: TaskModel) {
        databaseSource.removeTask(task.toEntity())
        networkSource.deleteTask(task).collect { networkState ->
            when (networkState) {
                is NetworkState.Failure -> committer.updateRevision()
                else -> {}
            }
        }
    }

    @Throws(ValidationException::class)
    suspend fun updateTask(task: TaskModel) {
        validateTask(task.text, task.deadline)
        databaseSource.updateTask(task.copy(modifyingTime = System.currentTimeMillis()).toEntity())
        networkSource.putTask(task).collect { networkState ->
            when (networkState) {
                is NetworkState.Failure -> committer.updateRevision()
                else -> {}
            }
        }
    }

    @Throws(ValidationException::class)
    private fun validateTask(text: String, deadline: Long?) {
        if (text.isBlank()) {
            throw ValidationException("Text is blank!")
        }
        if ((deadline ?: 0) < 0) {
            throw ValidationException("Deadline: $deadline is not valid!")
        }
    }

    private fun getUUID(): UUID = UUID.randomUUID()

    private fun buildTaskModel(
        text: String, priority: Priority, deadline: Long?
    ): TaskModel = TaskModel(
        getUUID(), text, priority, false, System.currentTimeMillis(), deadline, null
    )
}