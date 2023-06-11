package org.alexcawl.todoapp.data

import java.time.LocalDateTime

data class TodoItem(
    val identifier: String,
    val text: String,
    val priority: Priority,
    val isDone: Boolean,
    val creationTime: LocalDateTime,
    val modifyingTime: LocalDateTime?,
    val deadline: LocalDateTime?,
) {
    companion object {
        enum class Priority {
            LOW,
            NORMAL,
            HIGH
        }
    }

    constructor(
        identifier: String,
        text: String,
        priority: Priority,
        isDone: Boolean,
        creationTime: LocalDateTime,
    ) : this(
        identifier,
        text,
        priority,
        isDone,
        creationTime,
        null,
        null
    )
}
