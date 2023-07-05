package org.alexcawl.todoapp.data.service

import android.content.Context
import org.alexcawl.todoapp.data.database.datasource.DatabaseSource
import org.alexcawl.todoapp.data.network.datasource.NetworkSource
import org.alexcawl.todoapp.data.network.util.NetworkState
import org.alexcawl.todoapp.data.util.NetworkException
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.service.SynchronizeService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SynchronizeServiceImpl @Inject constructor(
    val context: Context,
    private val databaseSource: DatabaseSource,
    private val networkSource: NetworkSource,
) : SynchronizeService {
    private val sharedPreferences = context.getSharedPreferences("ToDoPref", 0)

    override fun get(): Int = sharedPreferences.getInt("REVISION", 0)

    override fun set(revision: Int) {
        sharedPreferences.edit().putInt("REVISION", revision).apply()
    }

    @Throws(NetworkException::class)
    override suspend fun synchronize() {
        val tasks = databaseSource.getTasksAsList()
        networkSource.getTasks().collect { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Failure -> throw NetworkException(state.cause.stackTraceToString())
                is NetworkState.Success -> compareData(get(), tasks, state.revision, state.data)
            }
        }
    }

    private suspend fun compareData(
        androidRevision: Int,
        androidData: List<TaskModel>,
        serverRevision: Int,
        serverData: List<TaskModel>
    ) {
        if (androidRevision < serverRevision || androidData != serverData) {
            when (mergeCondition(androidData, serverData)) {
                true -> {
                    databaseSource.overwriteDatabase(serverData)
                    set(serverRevision)
                }
                false -> networkSource.patchTasks(androidData, get()).collect { state ->
                    when (state) {
                        is NetworkState.Failure -> throw NetworkException(state.cause.stackTraceToString())
                        is NetworkState.Success -> set(state.revision)
                        else -> {}
                    }
                }
            }
        }
    }

    private fun mergeCondition(androidData: List<TaskModel>, serverData: List<TaskModel>): Boolean {
        val latestAndroidChangeTime = androidData.maxOfOrNull { it.modifyingTime } ?: 0
        val latestServerChangeTime = serverData.maxOfOrNull { it.modifyingTime } ?: 0
        return latestAndroidChangeTime < latestServerChangeTime
    }
}