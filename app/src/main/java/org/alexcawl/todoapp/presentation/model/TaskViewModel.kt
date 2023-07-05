package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.usecases.*
import org.alexcawl.todoapp.presentation.util.UiState
import java.util.*

class TaskViewModel(
    private val updateCase: UpdateTaskUseCase,
    private val getAllCase: GetTasksUseCase,
    private val getSingleCase: GetTaskUseCase,
    private val deleteCase: DeleteTaskUseCase,
    private val addCase: AddTaskUseCase,
    private val syncCase: SynchronizeUseCase
) : ViewModel() {
    private var job: Job? = null

    private val _visibility: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val visibility: StateFlow<Boolean> get() = _visibility

    private val _allTasks: MutableStateFlow<UiState<List<TaskModel>>> = MutableStateFlow(UiState.Start)
    val allTasks: StateFlow<UiState<List<TaskModel>>> get() = _allTasks

    private val _undoneTasks: MutableStateFlow<UiState<List<TaskModel>>> = MutableStateFlow(UiState.Start)
    val undoneTasks: StateFlow<UiState<List<TaskModel>>> get() = _undoneTasks

    init {
        job = viewModelScope.launch {
            getAllCase().collect { state ->
                when (state) {
                    is DataState.Result -> {
                        _allTasks.emit(UiState.Success(state.data))
                        _undoneTasks.emit(UiState.Success(state.data.filter { !it.isDone }))
                    }
                    is DataState.Exception -> {
                        _allTasks.emit(UiState.Error(state.cause.message ?: "WTF"))
                        _undoneTasks.emit(UiState.Error(state.cause.message ?: "WTF"))
                    }
                    else -> {
                        _allTasks.emit(UiState.Start)
                        _undoneTasks.emit(UiState.Start)
                    }
                }
            }
        }
    }

    fun addTask(text: String, priority: Priority, deadline: Long?): Flow<UiState<String>> = flow {
        addCase(text, priority, deadline).collect { state ->
            when (state) {
                is DataState.Initial -> emit(UiState.Start)
                is DataState.Result -> emit(UiState.Success("Task added!"))
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: "WTF"))
            }
        }
    }

    fun setTask(task: TaskModel): Flow<UiState<String>> = flow {
        updateCase(task).collect { state ->
            when (state) {
                is DataState.Initial -> emit(UiState.Start)
                is DataState.Result -> emit(UiState.Success("Task modified!"))
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: "WTF"))
            }
        }
    }

    fun removeTask(task: TaskModel): Flow<UiState<String>> = flow {
        deleteCase(task).collect { state ->
            when (state) {
                is DataState.Initial -> emit(UiState.Start)
                is DataState.Result -> emit(UiState.Success("Task deleted!"))
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: "WTF"))
            }
        }
    }

    fun requireTask(id: UUID): Flow<UiState<TaskModel>> = flow {
        getSingleCase(id).collect { state ->
            when (state) {
                is DataState.Result -> emit(UiState.Success(state.data))
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: "WTF"))
                else -> emit(UiState.Start)
            }
        }
    }

    fun synchronize(): Flow<UiState<String>> = flow {
        syncCase().collect { state ->
            when (state) {
                is DataState.Result -> emit(UiState.Success("Synchronized!"))
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: "WTF"))
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