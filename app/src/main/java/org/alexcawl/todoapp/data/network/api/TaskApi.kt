package org.alexcawl.todoapp.data.network.api

import org.alexcawl.todoapp.data.network.dto.request.TaskListRequestDto
import org.alexcawl.todoapp.data.network.dto.request.TaskSingleRequestDto
import org.alexcawl.todoapp.data.network.dto.response.TaskSingleResponseDto
import org.alexcawl.todoapp.data.network.dto.response.TaskListResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID


interface TaskApi {
    @GET("/list")
    fun getTasks(): Call<TaskListResponseDto>

    @PATCH("/list")
    fun patchTasks(@Header("X-Last-Known-Revision") header: Int, @Body body: TaskListRequestDto ): Call<TaskListResponseDto>

    @GET("/list/{id}")
    fun getTask(@Path("id") id: UUID): Call<TaskSingleResponseDto>

    @POST("/list/{id}")
    fun postTask(@Header("X-Last-Known-Revision") header: Int, @Path("id") id: UUID, @Body body: TaskSingleRequestDto): Call<TaskSingleResponseDto>

    @PUT("/list/{id}")
    fun putTask(@Path("id") id: UUID, @Body body: TaskSingleRequestDto): Call<TaskSingleResponseDto>

    @DELETE("/list/{id}")
    fun deleteTask(@Path("id") id: UUID): Call<TaskSingleResponseDto>
}