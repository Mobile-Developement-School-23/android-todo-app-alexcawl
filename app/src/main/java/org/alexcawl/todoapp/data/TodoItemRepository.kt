package org.alexcawl.todoapp.data

import org.alexcawl.todoapp.domain.model.TaskModel
import java.util.*

class TodoItemRepository private constructor() {
    private val items: MutableList<TaskModel> = mutableListOf()

    init {
        items.addAll(listOf(
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0, 50),
                TaskModel.Companion.Priority.NORMAL,
                false,
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0),
                TaskModel.Companion.Priority.NORMAL,
                false,
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0, 50),
                TaskModel.Companion.Priority.LOW,
                false,
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0, 50),
                TaskModel.Companion.Priority.NORMAL,
                true,
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0, 50),
                TaskModel.Companion.Priority.HIGH,
                false,
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0),
                TaskModel.Companion.Priority.NORMAL,
                true,
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0, 50),
                TaskModel.Companion.Priority.NORMAL,
                false,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0),
                TaskModel.Companion.Priority.NORMAL,
                false,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0),
                TaskModel.Companion.Priority.NORMAL,
                true,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0),
                TaskModel.Companion.Priority.HIGH,
                true,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0, 50),
                TaskModel.Companion.Priority.HIGH,
                false,
                System.currentTimeMillis()),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0),
                TaskModel.Companion.Priority.NORMAL,
                true,
                System.currentTimeMillis()),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0, 50),
                TaskModel.Companion.Priority.NORMAL,
                false,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0),
                TaskModel.Companion.Priority.NORMAL,
                false,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ),
            TaskModel(
                UUID.randomUUID(),
                LOREM_IPSUM.substring(0),
                TaskModel.Companion.Priority.NORMAL,
                true,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ),
        ))
    }

    companion object {
        const val LOREM_IPSUM: String =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad " + "minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea " + "commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse " + "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

        @Volatile
        private var instance: TodoItemRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: TodoItemRepository().also { instance = it }
        }
    }

    fun getItems(): List<TaskModel> = items
}