package org.alexcawl.todoapp.data.model

import java.util.Calendar

data class TodoItem(
    val identifier: String,
    var text: String,
    var priority: Priority,
    var isDone: Boolean,
    val creationTime: Calendar,
    var modifyingTime: Calendar?,
    var deadline: Calendar?,
) {
    companion object {
        enum class Priority {
            LOW,
            NORMAL,
            HIGH
        }

        fun of(value: TodoItem): TodoItem {
            return TodoItem(
                value.identifier,
                value.text,
                value.priority,
                value.isDone,
                value.creationTime,
                value.modifyingTime,
                value.deadline
            )
        }

        fun createEmpty(identifier: String, creationTime: Calendar): TodoItem {
            return TodoItem(
                identifier,
                "",
                Priority.NORMAL,
                false,
                creationTime,
                null,
                null
            )
        }
    }

    constructor(
        identifier: String,
        text: String,
        priority: Priority,
        isDone: Boolean,
        creationTime: Calendar,
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
