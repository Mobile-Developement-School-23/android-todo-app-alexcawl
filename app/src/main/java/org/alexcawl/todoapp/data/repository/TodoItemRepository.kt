package org.alexcawl.todoapp.data.repository

import org.alexcawl.todoapp.data.model.TodoItem
import java.util.*

class TodoItemRepository private constructor() {
    private val items: MutableList<TodoItem> = mutableListOf()

    init {
        items.addAll(listOf(
            TodoItem(
                "1",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.NORMAL,
                false,
                Calendar.getInstance()
            ),
            TodoItem(
                "2",
                LOREM_IPSUM.substring(0),
                TodoItem.Companion.Priority.NORMAL,
                false,
                Calendar.getInstance()
            ),
            TodoItem(
                "3",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.LOW,
                false,
                Calendar.getInstance()
            ),
            TodoItem(
                "4",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.NORMAL,
                true,
                Calendar.getInstance()
            ),
            TodoItem(
                "5",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.HIGH,
                false,
                Calendar.getInstance()
            ),
            TodoItem(
                "6",
                LOREM_IPSUM.substring(0),
                TodoItem.Companion.Priority.NORMAL,
                true,
                Calendar.getInstance()
            ),
            TodoItem(
                "7",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.NORMAL,
                false,
                Calendar.getInstance(),
                null,
                Calendar.getInstance()
            ),
            TodoItem(
                "8",
                LOREM_IPSUM.substring(0),
                TodoItem.Companion.Priority.NORMAL,
                false,
                Calendar.getInstance(),
                null,
                Calendar.getInstance()
            ),
            TodoItem(
                "9",
                LOREM_IPSUM.substring(0),
                TodoItem.Companion.Priority.NORMAL,
                true,
                Calendar.getInstance(),
                null,
                Calendar.getInstance()
            ),
            TodoItem(
                "10",
                LOREM_IPSUM.substring(0),
                TodoItem.Companion.Priority.HIGH,
                true,
                Calendar.getInstance(),
                null,
                Calendar.getInstance()
            )
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

    fun getItems(): List<TodoItem> = items

    fun addItem(todoItem: TodoItem) {
        items.add(todoItem)
    }
}