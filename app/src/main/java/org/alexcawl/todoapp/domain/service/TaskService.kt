package org.alexcawl.todoapp.domain.service

import kotlinx.coroutines.flow.*
import org.alexcawl.todoapp.data.database.usecases.*
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
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

    private val getAllCase = TaskEntityGetAllUseCase
    private val getUncompletedCase = TaskEntityGetUncompletedUseCase
    private val getByIdCase = TaskEntityGetByIdUseCase
    private val setCase = TaskEntitySetUseCase
    private val deleteCase = TaskEntityRemoveUseCase

    fun getAllTasks(): Flow<List<TaskModel>> = getAllCase()

    fun getUncompletedTasks(): Flow<List<TaskModel>> = getUncompletedCase()

    fun getTaskById(id: UUID): Flow<Result<TaskModel>> = getByIdCase(id)

    /**
    *
    * */
    @Throws(ValidationException::class)
    suspend fun addTask(text: String, priority: Priority, deadline: Long?) {
        validateTask(text, deadline)
        setCase(buildTaskModel(text, priority, deadline))
        Result
    }

    /**
     *
     * */
    suspend fun removeTask(task: TaskModel) = deleteCase(task)

    /**
     *
     * */
    @Throws(ValidationException::class)
    suspend fun setTask(task: TaskModel) {
        validateTask(task.text, task.deadline)
        setCase(task.copy(modifyingTime = System.currentTimeMillis()))
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

    private fun getUUID(): UUID = UUID.randomUUID()

    private fun buildTaskModel(
        text: String, priority: Priority, deadline: Long?
    ): TaskModel = TaskModel(
        getUUID(), text, priority, false, System.currentTimeMillis(), deadline, null
    )
}