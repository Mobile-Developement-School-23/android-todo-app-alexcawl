package org.alexcawl.todoapp.domain.source

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.data.network.util.NetworkState
import org.alexcawl.todoapp.domain.model.TaskModel
import java.util.UUID

interface INetworkSource {
    fun getTasks(
        token: String
    ): Flow<NetworkState<List<TaskModel>>>

    fun updateTasks(
        tasks: List<TaskModel>,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<List<TaskModel>>>

    fun addTask(
        task: TaskModel,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<TaskModel>>

    fun updateTask(
        task: TaskModel,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<TaskModel>>

    fun deleteTask(
        taskId: UUID,
        revision: Int,
        token: String,
    ): Flow<NetworkState<TaskModel>>
}