package org.alexcawl.todoapp.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.data.database.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(entity = TaskEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TaskEntity)

    @Delete(entity = TaskEntity::class)
    suspend fun removeTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: String): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>
}