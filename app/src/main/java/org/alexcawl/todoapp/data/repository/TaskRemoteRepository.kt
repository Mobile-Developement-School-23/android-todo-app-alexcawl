package org.alexcawl.todoapp.data.repository

import org.alexcawl.todoapp.data.network.util.NetworkException
import org.alexcawl.todoapp.data.network.util.NetworkState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.IAlarmScheduler
import org.alexcawl.todoapp.domain.repository.ISettingsRepository
import org.alexcawl.todoapp.domain.repository.ISynchronizer
import org.alexcawl.todoapp.domain.repository.ITaskRemoteRepository
import org.alexcawl.todoapp.domain.source.IDatabaseSource
import org.alexcawl.todoapp.domain.source.INetworkSource
import javax.inject.Inject

/**
 * Remote repository implementation with synchronization ability
 * @param settingsSource Shared preferences data source
 * @param databaseSource Room data source
 * @param networkSource Retrofit data source
 * @see ITaskRemoteRepository
 * @see ISynchronizer
 * */
class TaskRemoteRepository @Inject constructor(
    private val settingsSource: ISettingsRepository,
    private val databaseSource: IDatabaseSource,
    private val networkSource: INetworkSource,
    private val scheduler: IAlarmScheduler
) : ITaskRemoteRepository, ISynchronizer {
    @Throws(NetworkException::class)
    override suspend fun addTask(task: TaskModel) {
        if (settingsSource.getServerEnabled()) {
            networkSource.addTask(
                task,
                settingsSource.getRevision(),
                settingsSource.getToken(),
                settingsSource.getUsername()
            ).collect {
                when (it) {
                    is NetworkState.Failure -> synchronize()
                    is NetworkState.OK -> settingsSource.setRevision(it.revision)
                    else -> {}
                }
            }
        }
    }

    @Throws(NetworkException::class)
    override suspend fun updateTask(task: TaskModel) {
        if (settingsSource.getServerEnabled()) {
            networkSource.updateTask(
                task,
                settingsSource.getRevision(),
                settingsSource.getToken(),
                settingsSource.getUsername()
            ).collect {
                when (it) {
                    is NetworkState.Failure -> synchronize()
                    is NetworkState.OK -> settingsSource.setRevision(it.revision)
                    else -> {}
                }
            }
        }
    }

    @Throws(NetworkException::class)
    override suspend fun deleteTask(task: TaskModel) {
        if (settingsSource.getServerEnabled()) {
            networkSource.deleteTask(
                task.id,
                settingsSource.getRevision(),
                settingsSource.getToken()
            ).collect {
                when (it) {
                    is NetworkState.Failure -> synchronize()
                    is NetworkState.OK -> settingsSource.setRevision(it.revision)
                    else -> {}
                }
            }
        }
    }

    @Throws(NetworkException::class)
    override suspend fun synchronize() {
        if (settingsSource.getServerEnabled()) {
            networkSource.getTasks(settingsSource.getToken()).collect { state ->
                when (state) {
                    is NetworkState.Failure -> throw NetworkException("Internet is not available!")
                    is NetworkState.Success -> merge(state.revision, state.data)
                    else -> {}
                }
            }
        }
    }

    @Throws(NetworkException::class)
    private suspend fun merge(
        serverRevision: Int,
        serverData: List<TaskModel>
    ) {
        val localRevision: Int = settingsSource.getRevision()
        val localData: List<TaskModel> = databaseSource.getTasksAsList()
        val data: List<TaskModel> = mergeData(localRevision, localData, serverRevision, serverData)
        networkSource.updateTasks(
            data,
            settingsSource.getRevision(),
            settingsSource.getToken(),
            settingsSource.getUsername()
        ).collect { state ->
            when (state) {
                is NetworkState.Failure -> throw NetworkException("Internet is not available!")
                is NetworkState.Success -> {
                    databaseSource.overwrite(state.data)
                    settingsSource.setRevision(state.revision)
                    localData.forEach(scheduler::cancelNotification)
                    state.data.forEach(scheduler::scheduleNotification)
                }
                else -> {}
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