package org.alexcawl.todoapp.data.network.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.alexcawl.todoapp.data.network.api.TaskApi
import org.alexcawl.todoapp.data.network.dto.response.TaskDto
import org.alexcawl.todoapp.data.network.dto.response.TaskListResponseDto
import org.alexcawl.todoapp.data.util.DataState
import org.alexcawl.todoapp.data.util.toModel
import org.alexcawl.todoapp.domain.model.TaskModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkSource(
    private val api: TaskApi
) {
    fun getTasks(): Flow<DataState<List<TaskModel>>> = flow {
        emit(DataState.Initial)
        val tasks: MutableList<TaskDto> = mutableListOf()
        api.getTasks().enqueue(
            object : Callback<TaskListResponseDto> {
                override fun onResponse(
                    call: Call<TaskListResponseDto>, response: Response<TaskListResponseDto>
                ) {
                    println(response.body().toString())
                    tasks.addAll(response.body().list)
                }

                override fun onFailure(call: Call<TaskListResponseDto>, t: Throwable) =
                    call.cancel()
            }
        )
        emit(DataState.OK(tasks.map(TaskDto::toModel)))
    }
}