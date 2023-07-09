package org.alexcawl.todoapp.data.repository

import org.alexcawl.todoapp.data.network.util.NetworkState
import org.alexcawl.todoapp.data.util.NetworkException
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.ISynchronizer
import org.alexcawl.todoapp.domain.repository.ITaskRemoteRepository
import org.alexcawl.todoapp.domain.source.IDatabaseSource
import org.alexcawl.todoapp.domain.source.INetworkSource
import org.alexcawl.todoapp.domain.repository.IRevisionRepository
import javax.inject.Inject

/**
 * Remote repository implementation with synchronization ability
 * @param revisionSource Shared preferences data source
 * @param databaseSource Room data source
 * @param networkSource Retrofit data source
 * @see ITaskRemoteRepository
 * @see ISynchronizer
 * */
class TaskRemoteRepository @Inject constructor(
    private val revisionSource: IRevisionRepository,
    private val databaseSource: IDatabaseSource,
    private val networkSource: INetworkSource,
) : ITaskRemoteRepository, ISynchronizer {
    @Throws(NetworkException::class)
    override suspend fun addTask(task: TaskModel) {
        networkSource.addTask(task, revisionSource.getRevision()).collect {
            when (it) {
                is NetworkState.Failure -> synchronize()
                else -> {}
            }
        }
    }

    @Throws(NetworkException::class)
    override suspend fun updateTask(task: TaskModel) {
        networkSource.updateTask(task, revisionSource.getRevision()).collect {
            when (it) {
                is NetworkState.Failure -> synchronize()
                else -> {}
            }
        }
    }

    @Throws(NetworkException::class)
    override suspend fun deleteTask(task: TaskModel) {
        networkSource.deleteTask(task, revisionSource.getRevision()).collect {
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
        val localRevision: Int = revisionSource.getRevision()
        val localData: List<TaskModel> = databaseSource.getTasksAsList()
        val data: List<TaskModel> = mergeData(localRevision, localData, serverRevision, serverData)
        networkSource.updateTasks(data, localRevision).collect { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Failure -> throw NetworkException("Internet is not available!")
                is NetworkState.Success -> {
                    databaseSource.overwrite(state.data)
                    revisionSource.setRevision(state.revision)
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