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
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.usecases.*
import org.alexcawl.todoapp.presentation.util.UiState

/**
 * Main ViewModel in application
 * @param getAllCase Tasks get all use case
 * @param syncCase Task synchronizing use case
 * @see ViewModel
 * @see ViewModelFactory
 * */
class MainViewModel(
    private val getAllCase: IGetTasksUseCase,
    private val syncCase: ISyncUseCase,
    private val settingsUseCase: ISettingsOperateUseCase,
    private val updateCase: IUpdateTaskUseCase,
    private val deleteCase: IDeleteTaskUseCase
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
                        _allTasks.emit(UiState.Error(state.cause.message ?: state.cause.stackTraceToString()))
                        _undoneTasks.emit(UiState.Error(state.cause.message ?: state.cause.stackTraceToString()))
                    }
                    else -> {
                        _allTasks.emit(UiState.Start)
                        _undoneTasks.emit(UiState.Start)
                    }
                }
            }
        }
    }

    fun synchronize(): Flow<UiState<String>> = flow {
        syncCase().collect { state ->
            when (state) {
                is DataState.Result -> emit(UiState.Success("Synchronized!"))
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: state.cause.stackTraceToString()))
                else -> emit(UiState.Start)
            }
        }
    }

    fun updateTask(task: TaskModel): Flow<UiState<String>> = flow {
        updateCase(task).collect { state ->
            when (state) {
                is DataState.Initial -> emit(UiState.Start)
                is DataState.Result -> emit(UiState.Success("Task modified!"))
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: state.cause.stackTraceToString()))
            }
        }
    }

    fun deleteTask(task: TaskModel): Flow<UiState<String>> = flow {
        deleteCase(task).collect { state ->
            when (state) {
                is DataState.Initial -> emit(UiState.Start)
                is DataState.Result -> emit(UiState.Success("Task deleted!"))
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: state.cause.stackTraceToString()))
            }
        }
    }

    fun isServerEnabled(): Boolean = settingsUseCase.getServerEnabled()

    fun invertVisibilityState() {
        _visibility.value = _visibility.value.not()
    }

    override fun onCleared() {
        job?.cancel()
    }
}