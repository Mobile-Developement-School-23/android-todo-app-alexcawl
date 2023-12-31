package org.alexcawl.todoapp.data.network.api

import org.alexcawl.todoapp.data.network.dto.request.TaskListRequestDto
import org.alexcawl.todoapp.data.network.dto.request.TaskSingleRequestDto
import org.alexcawl.todoapp.data.network.dto.response.TaskListResponseDto
import org.alexcawl.todoapp.data.network.dto.response.TaskSingleResponseDto
import retrofit2.http.*
import java.util.*


interface TaskApi {
    @GET("list")
    suspend fun getTasks(
        @Header("Authorization") token: String
    ): TaskListResponseDto

    @PATCH("list")
    suspend fun patchTasks(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") header: Int,
        @Body body: TaskListRequestDto
    ):  TaskListResponseDto

    @GET("list/{id}")
    suspend fun getTask(
        @Header("Authorization") token: String,
        @Path("id") id: UUID
    ): TaskSingleResponseDto

    @POST("list")
    suspend fun postTask(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") header: Int,
        @Body body: TaskSingleRequestDto
    ): TaskSingleResponseDto

    @PUT("list/{id}")
    suspend fun putTask(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") header: Int,
        @Path("id") id: UUID,
        @Body body: TaskSingleRequestDto
    ): TaskSingleResponseDto

    @DELETE("list/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") header: Int,
        @Path("id") id: UUID
    ): TaskSingleResponseDto
}