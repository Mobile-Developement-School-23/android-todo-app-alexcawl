package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.data.util.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.usecases.*
import org.alexcawl.todoapp.domain.util.ValidationException
import java.util.*

class TaskViewModel(
    private val updateCase: TaskUpdateUseCase,
    private val getAllCase: TaskGetAllUseCase,
    private val getUncompletedCase: TaskGetUncompletedUseCase,
    private val getSingleCase: TaskGetByIdUseCase,
    private val removeCase: TaskRemoveUseCase,
    private val addCase: TaskAddUseCase
) : ViewModel() {
    private val _visibility: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val visibility: StateFlow<Boolean>
        get() = _visibility

    private val _allTasks: MutableStateFlow<DataState<List<TaskModel>>> =
        MutableStateFlow(DataState.Initial)
    val allTasks: StateFlow<DataState<List<TaskModel>>>
        get() = _allTasks

    private val _uncompletedTasks: MutableStateFlow<DataState<List<TaskModel>>> =
        MutableStateFlow(DataState.Initial)
    val uncompletedTasks: StateFlow<DataState<List<TaskModel>>>
        get() = _uncompletedTasks

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllCase().collect { state ->
                _allTasks.emit(state)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getUncompletedCase().collect { state ->
                _uncompletedTasks.emit(state)
            }
        }
    }

    @Throws(ValidationException::class)
    suspend fun removeTask(task: TaskModel) = removeCase(task)

    suspend fun addTask(
        text: String, priority: Priority, deadline: Long?
    ) = addCase(text, priority, deadline)

    @Throws(ValidationException::class)
    suspend fun setTask(task: TaskModel) = updateCase(task)

    fun requireTask(id: UUID) = getSingleCase(id)

    fun invertVisibilityState() {
        _visibility.value = _visibility.value.not()
    }
}