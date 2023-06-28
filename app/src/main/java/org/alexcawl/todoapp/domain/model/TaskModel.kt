package org.alexcawl.todoapp.domain.model

import org.alexcawl.todoapp.data.database.entity.TaskEntity
import java.util.*

data class TaskModel(
    val id: UUID,
    var text: String,
    var priority: Priority,
    var isDone: Boolean,
    val creationTime: Long,
    var deadline: Long? = null,
    var modifyingTime: Long? = null
) {
    constructor(id: UUID, creationTime: Long) : this(
        id, "", Priority.BASIC, false, creationTime
    )

    constructor(id: UUID) : this(
        id, "", Priority.BASIC, false, System.currentTimeMillis()
    )

    fun toEntity(): TaskEntity = TaskEntity(
        id.toString(), text, priority, isDone, creationTime, deadline, modifyingTime
    )
}
