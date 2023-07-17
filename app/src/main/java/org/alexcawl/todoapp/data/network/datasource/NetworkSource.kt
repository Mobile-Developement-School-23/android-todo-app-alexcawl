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
import org.alexcawl.todoapp.data.util.toToken
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.source.INetworkSource
import java.util.UUID
import javax.inject.Inject

/**
 * Retrofit data source
 * @param api Retrofit API
 * @see TaskApi
 * */
class NetworkSource @Inject constructor(
    private val api: TaskApi
) : INetworkSource {
    override fun getTasks(token: String): Flow<NetworkState<List<TaskModel>>> = flow {
        emit(NetworkState.Loading)
        val response = api.getTasks(token.toToken())
        emit(NetworkState.Success(response.list.map(TaskDto::toModel), response.revision))
    }.catch {
        NetworkState.Failure(it)
    }

    override fun updateTasks(
        tasks: List<TaskModel>, revision: Int, token: String, login: String
    ): Flow<NetworkState<List<TaskModel>>> = flow {
        emit(NetworkState.Loading)
        val response = api.patchTasks(
            token.toToken(),
            revision,
            TaskListRequestDto(tasks.map { it.toDto(login) })
        )
        emit(NetworkState.Success(response.list.map(TaskDto::toModel), response.revision))
    }.catch {
        emit(NetworkState.Failure(it))
    }

    override fun addTask(
        task: TaskModel, revision: Int, token: String, login: String
    ): Flow<NetworkState<TaskModel>> = flow {
        emit(NetworkState.Loading)
        val response = api.postTask(
            token.toToken(),
            revision,
            TaskSingleRequestDto(task.toDto(login))
        )
        emit(NetworkState.OK(response.revision))
    }.catch {
        emit(NetworkState.Failure(it))
    }

    override fun updateTask(
        task: TaskModel, revision: Int, token: String, login: String
    ): Flow<NetworkState<TaskModel>> = flow {
        emit(NetworkState.Loading)
        val response = api.putTask(
            token.toToken(),
            revision,
            task.id,
            TaskSingleRequestDto(task.toDto(login))
        )
        emit(NetworkState.OK(response.revision))
    }.catch {
        emit(NetworkState.Failure(it))
    }

    override fun deleteTask(
        taskId: UUID, revision: Int, token: String
    ): Flow<NetworkState<TaskModel>> = flow {
        emit(NetworkState.Loading)
        val response = api.deleteTask(token.toToken(), revision, taskId)
        emit(NetworkState.OK(response.revision))
    }.catch {
        emit(NetworkState.Failure(it))
    }
}