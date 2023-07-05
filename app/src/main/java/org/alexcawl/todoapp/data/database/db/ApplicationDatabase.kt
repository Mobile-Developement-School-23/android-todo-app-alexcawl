package org.alexcawl.todoapp.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.alexcawl.todoapp.data.database.dao.TaskDao
import org.alexcawl.todoapp.data.database.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 3, exportSchema = false)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}