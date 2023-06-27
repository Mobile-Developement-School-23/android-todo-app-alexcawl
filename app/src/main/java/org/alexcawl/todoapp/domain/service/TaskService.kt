package org.alexcawl.todoapp.domain.service

import kotlinx.coroutines.flow.*
import org.alexcawl.todoapp.data.TodoItemRepository
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.util.DuplicationException
import org.alexcawl.todoapp.domain.util.IllegalUUIDException
import org.alexcawl.todoapp.domain.util.ValidationException
import java.util.*

class TaskService private constructor() {
    companion object {
        @Volatile
        private var instance: TaskService? = null

        fun getInstance(): TaskService = instance ?: synchronized(this) {
            instance ?: TaskService().also { instance = it }
        }
    }

    private val repository = TodoItemRepository.getInstance()
    private val _tasks: MutableStateFlow<List<TaskModel>> = MutableStateFlow(repository.getItems())
    val tasks: StateFlow<List<TaskModel>>
        get() = _tasks

    suspend fun addTask(text: String, priority: TaskModel.Companion.Priority, deadline: Long?) {
        val list = _tasks.value.toMutableList()
        list.add(buildTaskModel(text, priority, deadline))
        _tasks.emit(list)
    }

    suspend fun removeTask(task: TaskModel) {
        val list = _tasks.value.toMutableList()
        list.remove(task)
        _tasks.emit(list)
    }

    suspend fun setTask(task: TaskModel) {
        val list = _tasks.value.toMutableList().map {
            when(it.id == task.id) {
                true -> task.copy(modifyingTime = System.currentTimeMillis())
                false -> it
            }
        }
        _tasks.emit(list)
    }

    suspend fun setTasks(tasks: List<TaskModel>) {
        _tasks.emit(tasks)
    }

    @Throws(ValidationException::class)
    private fun validateTask(text: String, deadline: Long?) {
        if (text.isBlank()) {
            throw ValidationException("Text is blank!")
        }
        if ((deadline ?: 0) < 0) {
            throw ValidationException("Deadline: $deadline is not valid!")
        }
    }

    @Throws(DuplicationException::class)
    private fun checkForDuplicates(
        text: String, priority: TaskModel.Companion.Priority, deadline: Long?, list: List<TaskModel>
    ) {
        when (val task = list.find {
            it.text == text && it.priority == priority && it.deadline == deadline
        }) {
            null -> {}
            else -> throw DuplicationException("Duplicate for $task")
        }
    }

    private fun getUUID(): UUID = UUID.randomUUID()

    @Throws(IllegalStateException::class)
    private fun validateUUID(uuid: UUID, list: List<TaskModel>): TaskModel {
        return when (val task = list.find { it.id == uuid }) {
            null -> throw IllegalUUIDException("UUID $uuid is not existing")
            else -> task
        }
    }

    private fun buildTaskModel(
        text: String,
        priority: TaskModel.Companion.Priority,
        deadline: Long?
    ): TaskModel = TaskModel(
        getUUID(), text, priority, false,
        System.currentTimeMillis(), null, deadline
    )
}