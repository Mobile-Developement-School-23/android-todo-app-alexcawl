package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.usecases.TaskAddUseCase
import org.alexcawl.todoapp.domain.usecases.TaskEditUseCase
import org.alexcawl.todoapp.domain.usecases.TaskGetUseCase
import org.alexcawl.todoapp.domain.usecases.TaskRemoveUseCase

class TaskViewModel : ViewModel() {
    private val addCase = TaskAddUseCase
    private val editCase = TaskEditUseCase
    private val getCase = TaskGetUseCase
    private val removeCase = TaskRemoveUseCase
    fun getAllTasks(): Flow<List<TaskModel>> = getCase()

    suspend fun removeTask(task: TaskModel): Unit = removeCase(task)

    suspend fun addTask(
        text: String,
        priority: TaskModel.Companion.Priority,
        deadline: Long?
    ): Unit = addCase(text, priority, deadline)

    suspend fun setTask(task: TaskModel): Unit = editCase(task)

    suspend fun setTasks(tasks: List<TaskModel>): Unit = editCase(tasks)
}