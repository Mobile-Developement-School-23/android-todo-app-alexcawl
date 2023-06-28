package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.usecases.*
import org.alexcawl.todoapp.domain.util.ValidationException
import java.util.*

class TaskViewModel : ViewModel() {
    private val addCase = TaskAddUseCase
    private val editCase = TaskEditUseCase
    private val getAllCase = TaskGetAllUseCase
    private val getUncompletedCase = TaskGetUncompletedUseCase
    private val getSingleCase = TaskGetByIdUseCase
    private val removeCase = TaskRemoveUseCase
    fun getAllTasks(): StateFlow<List<TaskModel>> = getAllCase()

    fun getUncompletedTasks(): StateFlow<List<TaskModel>> = getUncompletedCase()

    fun getTaskById(id: UUID): StateFlow<Result<TaskModel>> = getSingleCase(id)

    suspend fun removeTask(task: TaskModel): Unit = removeCase(task)

    @Throws(ValidationException::class)
    suspend fun addTask(
        text: String,
        priority: Priority,
        deadline: Long?
    ): Unit = addCase(text, priority, deadline)

    @Throws(ValidationException::class)
    suspend fun setTask(task: TaskModel): Unit = editCase(task)
}