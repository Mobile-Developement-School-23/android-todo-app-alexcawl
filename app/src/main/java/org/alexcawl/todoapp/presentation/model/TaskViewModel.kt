package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.usecases.*
import org.alexcawl.todoapp.presentation.util.UiState
import java.util.*

class TaskViewModel(
    private val updateCase: TaskUpdateUseCase,
    private val getAllCase: TaskGetAllUseCase,
    private val getSingleCase: TaskGetByIdUseCase,
    private val removeCase: TaskRemoveUseCase,
    private val addCase: TaskAddUseCase
) : ViewModel() {
    private val _visibility: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val visibility: StateFlow<Boolean>
        get() = _visibility

    private var job: Job? = null
    private val _tasks: MutableStateFlow<UiState<List<TaskModel>>> =
        MutableStateFlow(UiState.Start)
    val getAll: StateFlow<UiState<List<TaskModel>>>
        get() = _tasks

    private val _undoneTasks: MutableStateFlow<UiState<List<TaskModel>>> =
        MutableStateFlow(UiState.Start)
    val getUndone: StateFlow<UiState<List<TaskModel>>>
        get() = _undoneTasks

    init {
        job = viewModelScope.launch(Dispatchers.IO) {
            getAllCase().collect { state ->
                when (state) {
                    is DataState.Deprecated -> {
                        _tasks.emit(UiState.Success(
                            state.data, "Data is deprecated!")
                        )
                        _undoneTasks.emit(UiState.Success(
                            state.data.filter { !it.isDone }, "Data is deprecated!")
                        )
                    }
                    is DataState.Latest -> {
                        _tasks.emit(UiState.Success(state.data))
                        _undoneTasks.emit(UiState.Success(state.data.filter { !it.isDone }))
                    }
                    is DataState.Exception -> {
                        _tasks.emit(UiState.Error(state.cause.message ?: ""))
                        _undoneTasks.emit(UiState.Error(state.cause.message ?: ""))
                    }
                    else -> {
                        _tasks.emit(UiState.Start)
                        _undoneTasks.emit(UiState.Start)
                    }
                }
            }
        }
    }

    fun addTask(text: String, priority: Priority, deadline: Long?): Flow<UiState<String>> = flow {
        emit(UiState.Start)
        addCase(text, priority, deadline)
        emit(UiState.Success("Task added!"))
    }.catch {
        emit(UiState.Error(it.message ?: "Unrecognized exception!"))
    }

    fun setTask(task: TaskModel): Flow<UiState<String>> = flow {
        emit(UiState.Start)
        updateCase(task)
        emit(UiState.Success("Task modified!"))
    }.catch {
        emit(UiState.Error(it.stackTraceToString()))
    }

    suspend fun removeTask(task: TaskModel): Flow<UiState<String>> = flow {
        emit(UiState.Start)
        removeCase(task)
        emit(UiState.Success("Task deleted!"))
    }.catch {
        emit(UiState.Error(it.stackTraceToString()))
    }

    fun requireTask(id: UUID): Flow<UiState<TaskModel>> = flow {
        getSingleCase(id).collect { dataState ->
            when (dataState) {
                is DataState.Deprecated -> emit(
                    UiState.Success(dataState.data, "Data is deprecated!")
                )
                is DataState.Latest -> emit(UiState.Success(dataState.data))
                is DataState.Exception -> emit(UiState.Error(dataState.cause.message ?: ""))
                else -> emit(UiState.Start)
            }
        }
    }

    fun invertVisibilityState() {
        _visibility.value = _visibility.value.not()
    }

    override fun onCleared() {
        job?.cancel()
    }
}