package org.alexcawl.todoapp.data

import java.time.LocalDateTime

data class TodoItem(
    val identifier: String,
    var text: String,
    var priority: Priority,
    var isDone: Boolean,
    val creationTime: LocalDateTime,
    var modifyingTime: LocalDateTime?,
    var deadline: LocalDateTime?,
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
