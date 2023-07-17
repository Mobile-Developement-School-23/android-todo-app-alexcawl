package org.alexcawl.todoapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.alexcawl.todoapp.domain.model.Priority

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "priority") val priority: Priority,
    @ColumnInfo(name = "is_done") val isDone: Boolean,
    @ColumnInfo(name = "creation_time") val creationTime: Long,
    @ColumnInfo(name = "modifying_time") val modifyingTime: Long,
    @ColumnInfo(name = "deadline") val deadline: Long? = null
)
