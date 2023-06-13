package org.alexcawl.todoapp.extensions

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun LocalDateTime?.toLong(): Long? {
    return when(this) {
        null -> null
        else -> ZonedDateTime.of(this, ZoneId.systemDefault()).toEpochSecond()
    }
}