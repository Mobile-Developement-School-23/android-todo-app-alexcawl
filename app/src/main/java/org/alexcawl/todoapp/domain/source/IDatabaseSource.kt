package org.alexcawl.todoapp.domain.source

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.data.database.util.RoomState
import org.alexcawl.todoapp.domain.model.TaskModel
import java.util.UUID

interface IDatabaseSource {
    fun getTasks(): Flow<RoomState<List<TaskModel>>>

    fun getTask(uuid: UUID): Flow<RoomState<TaskModel>>

    suspend fun addTask(task: TaskModel)

    suspend fun deleteTask(task: TaskModel)

    /*
    * Synchronous method in order not to cycle in Flow data
    * */
    fun getTasksAsList(): List<TaskModel>

    /*
    * Synchronous overwriting database
    * */
    fun overwrite(tasks: List<TaskModel>)
}