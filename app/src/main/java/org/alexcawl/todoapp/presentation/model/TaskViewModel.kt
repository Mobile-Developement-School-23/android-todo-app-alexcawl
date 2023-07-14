package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.usecases.IDeleteTaskUseCase
import org.alexcawl.todoapp.domain.usecases.IGetTaskUseCase
import org.alexcawl.todoapp.domain.usecases.IUpdateTaskUseCase
import org.alexcawl.todoapp.presentation.util.UiState
import java.util.*

class TaskViewModel(
    private val getSingleCase: IGetTaskUseCase,
    private val updateCase: IUpdateTaskUseCase,
    private val deleteCase: IDeleteTaskUseCase,
) : ViewModel() {
    private val _task: MutableStateFlow<TaskModel> = MutableStateFlow(TaskModel(UUID.randomUUID()))
    val task: StateFlow<TaskModel> = _task.asStateFlow()

    private val _text: MutableStateFlow<String> = MutableStateFlow("")
    val text: StateFlow<String> = _text.asStateFlow()

    private val _priority: MutableStateFlow<Priority> = MutableStateFlow(Priority.BASIC)
    val priority: StateFlow<Priority> = _priority.asStateFlow()

    private val _deadline: MutableStateFlow<Long?> = MutableStateFlow(null)
    val deadline: StateFlow<Long?> = _deadline.asStateFlow()

    private val _createdAt: MutableStateFlow<Long> = MutableStateFlow(0)
    val createdAt: StateFlow<Long> = _createdAt.asStateFlow()

    private val _changedAt: MutableStateFlow<Long> = MutableStateFlow(0)
    val changedAt: StateFlow<Long> = _changedAt.asStateFlow()

    fun loadTask(id: UUID): Flow<UiState<Any>> = flow {
        getSingleCase(id).collect { state ->
            emit(when (state) {
                is DataState.Initial -> UiState.Start
                is DataState.Result -> UiState.OK.also { load(state.data) }
                is DataState.Exception -> UiState.Error(
                    state.cause.message ?: state.cause.stackTraceToString()
                )
            })
        }
    }

    fun update(): Flow<UiState<Any>> = flow {
        emit(UiState.Start)
        task.collect { currentTask ->
            updateCase(
                currentTask.copy(
                    text = text.value, priority = priority.value, deadline = deadline.value
                )
            ).collect { state ->
                emit(
                    when (state) {
                        is DataState.Initial -> UiState.Start
                        is DataState.Result -> UiState.OK
                        is DataState.Exception -> UiState.Error(
                            state.cause.message ?: state.cause.stackTraceToString()
                        )
                    }
                )
            }
        }
    }

    fun delete(): Flow<UiState<Any>> = flow {
        task.collect { currentTask ->
            deleteCase(currentTask).collect { state ->
                emit(
                    when (state) {
                        is DataState.Initial -> UiState.Start
                        is DataState.Result -> UiState.OK
                        is DataState.Exception -> UiState.Error(
                            state.cause.message ?: state.cause.stackTraceToString()
                        )
                    }
                )
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

    private suspend fun load(task: TaskModel) {
        _task.emit(task)
        _text.emit(task.text)
        _priority.emit(task.priority)
        _deadline.emit(task.deadline)
        _createdAt.emit(task.creationTime)
        _changedAt.emit(task.modifyingTime)
    }
}