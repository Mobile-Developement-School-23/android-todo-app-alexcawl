package org.alexcawl.todoapp.data.util

import org.alexcawl.todoapp.data.database.entity.TaskEntity
import org.alexcawl.todoapp.data.network.dto.response.TaskDto
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import java.util.*

fun TaskEntity.toModel(): TaskModel = TaskModel(
    UUID.fromString(id), text, priority, isDone, creationTime, modifyingTime, deadline
)

fun TaskModel.toEntity(): TaskEntity = TaskEntity(
    id.toString(), text, priority, isDone, creationTime, modifyingTime, deadline
)

fun TaskModel.toDto(login: String): TaskDto = TaskDto(
    id,
    text,
    priority.toString(),
    deadline,
    isDone,
    null,
    creationTime,
    modifyingTime,
    login
)

fun TaskDto.toModel(): TaskModel = TaskModel(
    id,
    text,
    Priority.valueOf(importance.uppercase(Locale.ROOT)),
    done,
    createdAt,
    changedAt,
    deadline
)

fun String.toToken(): String = "Bearer $this"
