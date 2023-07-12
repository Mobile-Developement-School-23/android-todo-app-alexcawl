package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.usecases.IAddTaskUseCase
import org.alexcawl.todoapp.domain.usecases.IDeleteTaskUseCase
import org.alexcawl.todoapp.domain.usecases.IGetTaskUseCase
import org.alexcawl.todoapp.domain.usecases.IUpdateTaskUseCase
import org.alexcawl.todoapp.presentation.util.UiState
import java.util.*

class TaskViewModel(
    private val getSingleCase: IGetTaskUseCase,
    private val updateCase: IUpdateTaskUseCase,
    private val deleteCase: IDeleteTaskUseCase,
    private val addCase: IAddTaskUseCase
) : ViewModel() {
    private val _task: MutableStateFlow<TaskModel> = MutableStateFlow(TaskModel(UUID.randomUUID()))
    val task: StateFlow<TaskModel> = _task.asStateFlow()

    private val _text: MutableStateFlow<String> = MutableStateFlow("")
    val text: StateFlow<String> = _text.asStateFlow()

    private val _priority: MutableStateFlow<Priority> = MutableStateFlow(Priority.BASIC)
    val priority: StateFlow<Priority> = _priority.asStateFlow()

    private val _isDone: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDone: StateFlow<Boolean> = _isDone.asStateFlow()

    private val _deadline: MutableStateFlow<Long?> = MutableStateFlow(null)
    val deadline: StateFlow<Long?> = _deadline.asStateFlow()

    private val _createdAt: MutableStateFlow<Long> = MutableStateFlow(0)
    val createdAt: StateFlow<Long> = _createdAt.asStateFlow()

    private val _changedAt: MutableStateFlow<Long> = MutableStateFlow(0)
    val changedAt: StateFlow<Long> = _changedAt.asStateFlow()

    fun loadTask(id: UUID): Flow<UiState<String>> = flow {
        getSingleCase(id).collect { state ->
            when (state) {
                is DataState.Result -> {
                    val task = state.data
                    _task.emit(task)
                    _text.emit(task.text)
                    _priority.emit(task.priority)
                    _isDone.emit(task.isDone)
                    _deadline.emit(task.deadline)
                    _createdAt.emit(task.creationTime)
                    _changedAt.emit(task.modifyingTime)
                    emit(UiState.Success("OK"))
                }
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: state.cause.stackTraceToString()))
                else -> emit(UiState.Start)
            }
        }
    }

    fun addTask(): Flow<UiState<String>> = flow {
        val taskText: String = text.value
        val taskPriority: Priority = priority.value
        val taskDeadline: Long? = deadline.value
        addCase(taskText, taskPriority, taskDeadline).collect { state ->
            when (state) {
                is DataState.Initial -> emit(UiState.Start)
                is DataState.Result -> emit(UiState.Success("Task added!"))
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: state.cause.stackTraceToString()))
            }
            clear()
        }
    }.catch {
        emit(UiState.Error(it.cause?.message.toString()))
    }

    fun update(): Flow<UiState<String>> = flow {
        emit(UiState.Start)
        task.collect { currentTask ->
            updateCase(currentTask.copy(text = text.value, priority = priority.value, deadline = deadline.value)).collect { state ->
                when (state) {
                    is DataState.Initial -> emit(UiState.Start)
                    is DataState.Result -> emit(UiState.Success("Task modified!"))
                    is DataState.Exception -> emit(UiState.Error(state.cause.message ?: state.cause.stackTraceToString()))
                }
                clear()
            }
        }
    }

    fun delete(): Flow<UiState<String>> = flow {
        task.collect { currentTask ->
            deleteCase(currentTask).collect { state ->
                when (state) {
                    is DataState.Initial -> emit(UiState.Start)
                    is DataState.Result -> emit(UiState.Success("Task deleted!"))
                    is DataState.Exception -> emit(UiState.Error(state.cause.message ?: state.cause.stackTraceToString()))
                }
                clear()
            }
        }
    }

    fun setTaskText(text: String) {
        viewModelScope.launch {
            _text.emit(text)
        }
    }

    fun setTaskPriority(priority: Priority) {
        viewModelScope.launch {
            _priority.emit(priority)
        }
    }

    fun setTaskDeadline(deadline: Long?) {
        viewModelScope.launch {
            _deadline.emit(deadline)
        }
    }

    private fun clear() {
        viewModelScope.launch {
            _task.emit(TaskModel(UUID.randomUUID()))
            _text.emit("")
            _priority.emit(Priority.BASIC)
            _isDone.emit(false)
            _deadline.emit(null)
            _createdAt.emit(0)
            _changedAt.emit(0)
        }
    }
}