package org.alexcawl.todoapp.domain.source

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.data.network.util.NetworkState
import org.alexcawl.todoapp.domain.model.TaskModel

interface INetworkSource {
    fun getTasks(): Flow<NetworkState<List<TaskModel>>>

    fun updateTasks(tasks: List<TaskModel>, revision: Int): Flow<NetworkState<List<TaskModel>>>

    fun addTask(task: TaskModel, revision: Int): Flow<NetworkState<TaskModel>>

    fun updateTask(task: TaskModel, revision: Int): Flow<NetworkState<TaskModel>>

    fun deleteTask(task: TaskModel, revision: Int): Flow<NetworkState<TaskModel>>
}