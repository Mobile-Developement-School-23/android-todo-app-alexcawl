package org.alexcawl.todoapp.data.network.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.alexcawl.todoapp.data.network.api.TaskApi
import org.alexcawl.todoapp.data.network.dto.request.TaskListRequestDto
import org.alexcawl.todoapp.data.network.dto.request.TaskSingleRequestDto
import org.alexcawl.todoapp.data.network.dto.response.TaskDto
import org.alexcawl.todoapp.data.network.util.NetworkState
import org.alexcawl.todoapp.data.util.toDto
import org.alexcawl.todoapp.data.util.toModel
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.source.INetworkSource
import javax.inject.Inject

/**
 * Retrofit data source
 * @param api Retrofit API
 * @see TaskApi
 * */
class NetworkSource @Inject constructor(
    private val api: TaskApi
): INetworkSource {
    override fun getTasks(): Flow<NetworkState<List<TaskModel>>> = flow {
        emit(NetworkState.Loading)
        val response = api.getTasks()
        emit(NetworkState.Success(response.list.map(TaskDto::toModel), response.revision))
    }.catch {
        NetworkState.Failure(it)
    }

    override fun updateTasks(tasks: List<TaskModel>, revision: Int): Flow<NetworkState<List<TaskModel>>> =
        flow {
            emit(NetworkState.Loading)
            val response = api.patchTasks(revision, TaskListRequestDto(tasks.map(TaskModel::toDto)))
            emit(NetworkState.Success(response.list.map(TaskDto::toModel), response.revision))
        }.catch {
            emit(NetworkState.Failure(it))
        }

    override fun addTask(task: TaskModel, revision: Int): Flow<NetworkState<TaskModel>> = flow {
        emit(NetworkState.Loading)
        val response = api.postTask(revision, TaskSingleRequestDto(task.toDto()))
        emit(NetworkState.Success(response.element.toModel(), response.revision))
    }.catch {
        emit(NetworkState.Failure(it))
    }

    override fun updateTask(task: TaskModel, revision: Int): Flow<NetworkState<TaskModel>> = flow {
        emit(NetworkState.Loading)
        val response = api.putTask(revision, task.id, TaskSingleRequestDto(task.toDto()))
        emit(NetworkState.Success(response.element.toModel(), response.revision))
    }.catch {
        emit(NetworkState.Failure(it))
    }

    override fun deleteTask(task: TaskModel, revision: Int): Flow<NetworkState<TaskModel>> = flow {
        emit(NetworkState.Loading)
        val response = api.deleteTask(revision, task.id)
        emit(NetworkState.Success(response.element.toModel(), response.revision))
    }.catch {
        emit(NetworkState.Failure(it))
    }
}