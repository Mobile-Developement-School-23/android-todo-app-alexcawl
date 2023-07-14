package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.usecases.IAddTaskUseCase
import org.alexcawl.todoapp.presentation.util.UiState

class NewTaskViewModel(
    private val addCase: IAddTaskUseCase
): ViewModel() {
    private val _text: MutableStateFlow<String> = MutableStateFlow("")
    val text: StateFlow<String> = _text.asStateFlow()

    private val _priority: MutableStateFlow<Priority> = MutableStateFlow(Priority.BASIC)
    val priority: StateFlow<Priority> = _priority.asStateFlow()

    private val _deadline: MutableStateFlow<Long?> = MutableStateFlow(null)
    val deadline: StateFlow<Long?> = _deadline.asStateFlow()

    fun addTask(): Flow<UiState<Any>> = flow {
        val taskText: String = text.value
        val taskPriority: Priority = priority.value
        val taskDeadline: Long? = deadline.value
        addCase(taskText, taskPriority, taskDeadline).collect { state ->
            when (state) {
                is DataState.Initial -> emit(UiState.Start)
                is DataState.Result -> emit(UiState.OK)
                is DataState.Exception -> emit(UiState.Error(state.cause.message ?: state.cause.stackTraceToString()))
            }
        }
    }.catch {
        emit(UiState.Error(it.cause?.message.toString()))
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
}