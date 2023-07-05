package org.alexcawl.todoapp.domain.model

import java.util.*

data class TaskModel(
    val id: UUID,
    var text: String,
    var priority: Priority,
    var isDone: Boolean,
    val creationTime: Long,
    var modifyingTime: Long,
    var deadline: Long? = null,
) {
    constructor(id: UUID, creationTime: Long) : this(
        id, "", Priority.BASIC, false, creationTime, creationTime
    )

    constructor(id: UUID) : this(
        id, "", Priority.BASIC, false, System.currentTimeMillis(), System.currentTimeMillis()
    )
}
