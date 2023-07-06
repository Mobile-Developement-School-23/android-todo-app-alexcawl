package org.alexcawl.todoapp.data.repository

import org.alexcawl.todoapp.data.database.datasource.DatabaseSource
import org.alexcawl.todoapp.data.network.datasource.NetworkSource
import org.alexcawl.todoapp.data.network.util.NetworkState
import org.alexcawl.todoapp.data.preferences.datasource.PreferenceSource
import org.alexcawl.todoapp.data.util.NetworkException
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.Synchronizer
import org.alexcawl.todoapp.domain.repository.TaskRemoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRemoteRepositoryImpl @Inject constructor(
    private val prefSource: PreferenceSource,
    private val databaseSource: DatabaseSource,
    private val networkSource: NetworkSource,
) : TaskRemoteRepository, Synchronizer {
    @Throws(NetworkException::class)
    override suspend fun addTask(task: TaskModel) {
        networkSource.postTask(task, prefSource.getRevision()).collect {
            when (it) {
                is NetworkState.Failure -> synchronize()
                else -> {}
            }
        }
    }

    @Throws(NetworkException::class)
    override suspend fun updateTask(task: TaskModel) {
        networkSource.putTask(task, prefSource.getRevision()).collect {
            when (it) {
                is NetworkState.Failure -> synchronize()
                else -> {}
            }
        }
    }

    @Throws(NetworkException::class)
    override suspend fun deleteTask(task: TaskModel) {
        networkSource.deleteTask(task, prefSource.getRevision()).collect {
            when (it) {
                is NetworkState.Failure -> synchronize()
                else -> {}
            }
        }
    }

    @Throws(NetworkException::class)
    override suspend fun synchronize() {
        networkSource.getTasks().collect { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Failure -> throw NetworkException("Internet is not available!")
                is NetworkState.Success -> merge(state.revision, state.data)
            }
        }
    }

    @Throws(NetworkException::class)
    private suspend fun merge(
        serverRevision: Int,
        serverData: List<TaskModel>
    ) {
        val localRevision: Int = prefSource.getRevision()
        val localData: List<TaskModel> = databaseSource.getTasksAsList()
        val data: List<TaskModel> = mergeData(localRevision, localData, serverRevision, serverData)
        networkSource.patchTasks(data, localRevision).collect { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Failure -> throw NetworkException("Internet is not available!")
                is NetworkState.Success -> {
                    databaseSource.overwriteDatabase(state.data)
                    prefSource.setRevision(state.revision)
                }
            }
        }
    }

    private fun mergeData(
        localRevision: Int,
        localData: List<TaskModel>,
        serverRevision: Int,
        serverData: List<TaskModel>
    ): List<TaskModel> {
        val isLocalDataActual: Boolean = localRevision >= serverRevision
        return listOf(
            localData.map { Pair(true, it) },
            serverData.map { Pair(false, it) }
        ).flatten().groupBy { it.second.id }.map {
            when (it.value.size) {
                1 -> when (it.value.first().first == isLocalDataActual) {
                    true -> it.value.first().second
                    else -> null
                }
                else -> it.value.maxByOrNull { pair ->
                    maxOf(
                        pair.second.creationTime,
                        pair.second.modifyingTime
                    )
                }?.second
            }
        }.filterNotNull()
    }
}