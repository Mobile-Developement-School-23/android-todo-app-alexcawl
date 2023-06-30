package org.alexcawl.todoapp.data.network.datasource

import android.util.Log
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
import org.alexcawl.todoapp.presentation.util.PreferencesCommitter

class NetworkSource(
    private val committer: PreferencesCommitter,
    private val api: TaskApi
) {
    fun getTasks(): Flow<NetworkState<List<TaskModel>>> = flow {
        emit(NetworkState.Loading)
        val response = api.getTasks()
        Log.d("SHIT", response.list.toString())
        emit(NetworkState.Success(response.list.map(TaskDto::toModel), response.revision))
    }.catch {
        NetworkState.Failure(it)
    }

    fun patchTasks(list: List<TaskModel>): Flow<NetworkState<List<TaskModel>>> = flow {
        emit(NetworkState.Loading)
        val revision = committer.getRevision()
        val response = api.patchTasks(
            revision,
            TaskListRequestDto(list.map(TaskModel::toDto))
        )
        committer.setRevision(response.revision)
        emit(NetworkState.Success(response.list.map(TaskDto::toModel), response.revision))
    }.catch {
        emit(NetworkState.Failure(it))
    }

    fun postTask(task: TaskModel): Flow<NetworkState<TaskModel>> = flow {
        emit(NetworkState.Loading)
        val revision = committer.getRevision()
        val response = api.postTask(revision, TaskSingleRequestDto(task.toDto()))
        committer.setRevision(response.revision)
        emit(NetworkState.Success(response.element.toModel(), response.revision))
    }.catch {
        emit(NetworkState.Failure(it))
    }

    fun putTask(task: TaskModel): Flow<NetworkState<TaskModel>> = flow {
        emit(NetworkState.Loading)
        val revision = committer.getRevision()
        val response = api.putTask(revision, task.id, TaskSingleRequestDto(task.toDto()))
        committer.setRevision(response.revision)
        emit(NetworkState.Success(response.element.toModel(), response.revision))
    }.catch {
        emit(NetworkState.Failure(it))
    }

    fun deleteTask(task: TaskModel): Flow<NetworkState<TaskModel>> = flow {
        emit(NetworkState.Loading)
        val revision = committer.getRevision()
        val response = api.deleteTask(revision, task.id)
        committer.setRevision(response.revision)
        emit(NetworkState.Success(response.element.toModel(), revision))
    }    .catch {
        emit(NetworkState.Failure(it))
    }
}