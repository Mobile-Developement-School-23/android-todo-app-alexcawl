package org.alexcawl.todoapp.domain.model

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
    companion object {
        enum class Priority {
            LOW,
            NORMAL,
            HIGH
        }
    }

    constructor(id: UUID, creationTime: Long) : this(
        id,
        "",
        Priority.NORMAL,
        false,
        creationTime
    )

    constructor(id: UUID) : this(
        id,
        "",
        Priority.NORMAL,
        false,
        System.currentTimeMillis()
    )
}
