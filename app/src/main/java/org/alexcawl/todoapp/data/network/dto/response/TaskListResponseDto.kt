package org.alexcawl.todoapp.data.network.dto.response

import com.google.gson.annotations.SerializedName

data class TaskListResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<TaskDto>,
    @SerializedName("revision") val revision: String
)
