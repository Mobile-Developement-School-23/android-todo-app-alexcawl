package org.alexcawl.todoapp.domain.model

enum class Priority {
    LOW,
    BASIC,
    IMPORTANT;

    override fun toString(): String {
        return when(this) {
            LOW -> "low"
            BASIC -> "basic"
            IMPORTANT -> "important"
        }
    }
}