package org.alexcawl.todoapp.data.repository

import org.alexcawl.todoapp.data.model.TodoItem
import java.time.LocalDateTime

class TodoItemRepository private constructor() {
    companion object {
        const val LOREM_IPSUM: String =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad " + "minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea " + "commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse " + "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

        @Volatile
        private var instance: TodoItemRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: TodoItemRepository().also { instance = it }
        }
    }

    fun getItems(): List<TodoItem> {
        return listOf(
            TodoItem(
                "1",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "2",
                LOREM_IPSUM.substring(0),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "3",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.LOW,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "4",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.NORMAL,
                true,
                LocalDateTime.now()
            ),
            TodoItem(
                "5",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.HIGH,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "6",
                LOREM_IPSUM.substring(0),
                TodoItem.Companion.Priority.NORMAL,
                true,
                LocalDateTime.now()
            ),
            TodoItem(
                "7",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now(),
                null,
                LocalDateTime.now()
            ),
            TodoItem(
                "8",
                LOREM_IPSUM.substring(0),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now(),
                null,
                LocalDateTime.now()
            ),
            TodoItem(
                "9",
                LOREM_IPSUM.substring(0),
                TodoItem.Companion.Priority.NORMAL,
                true,
                LocalDateTime.now(),
                null,
                LocalDateTime.now()
            ),
            TodoItem(
                "10",
                LOREM_IPSUM.substring(0),
                TodoItem.Companion.Priority.HIGH,
                true,
                LocalDateTime.now(),
                null,
                LocalDateTime.now()
            ),
            TodoItem(
                "11",
                LOREM_IPSUM.substring(0, 20),
                TodoItem.Companion.Priority.LOW,
                true,
                LocalDateTime.now(),
                null,
                LocalDateTime.now()
            ),
            TodoItem(
                "12",
                LOREM_IPSUM.substring(0, 50),
                TodoItem.Companion.Priority.LOW,
                false,
                LocalDateTime.now(),
                null,
                LocalDateTime.now()
            ),
            TodoItem(
                "13",
                LOREM_IPSUM.substring(0, 100),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "14",
                LOREM_IPSUM.substring(0, 20),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "15",
                LOREM_IPSUM.substring(0, 20),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "16",
                LOREM_IPSUM.substring(0, 20),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "17",
                LOREM_IPSUM.substring(0, 20),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "18",
                LOREM_IPSUM.substring(0, 20),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "19",
                LOREM_IPSUM.substring(0, 20),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            ),
            TodoItem(
                "20",
                LOREM_IPSUM.substring(0, 20),
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            )
        )
    }
}