package org.alexcawl.todoapp.android.application

import android.app.Application
import org.alexcawl.todoapp.data.repository.TodoItemRepository
import org.alexcawl.todoapp.service.ConverterService

class TodoApplication : Application() {
    val converterService: ConverterService = ConverterService.getInstance()
    val todoItemRepository: TodoItemRepository = TodoItemRepository.getInstance()

    companion object {
        const val IDENTIFIER: String = "identifier"
    }
}