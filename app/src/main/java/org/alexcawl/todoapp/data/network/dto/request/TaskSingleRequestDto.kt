package org.alexcawl.todoapp.data.network.dto.request

import com.google.gson.annotations.SerializedName
import org.alexcawl.todoapp.data.network.dto.response.TaskDto

data class TaskSingleRequestDto(
    @SerializedName("element") val element: TaskDto
)