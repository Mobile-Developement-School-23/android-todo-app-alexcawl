package org.alexcawl.todoapp.data.network.dto.response

import com.google.gson.annotations.SerializedName

data class TaskSingleResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("element") val element: TaskDto,
    @SerializedName("revision") val revision: Int
)
