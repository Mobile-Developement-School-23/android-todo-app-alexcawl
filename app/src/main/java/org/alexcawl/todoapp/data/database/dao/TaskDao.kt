package org.alexcawl.todoapp.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.data.database.entity.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTask(id: String): Flow<TaskEntity?>

    @Insert(entity = TaskEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: TaskEntity)

    @Delete(entity = TaskEntity::class)
    suspend fun removeTask(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Insert(entity = TaskEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTasks(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks")
    suspend fun removeTasks()
}