package org.alexcawl.todoapp.android.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.alexcawl.todoapp.data.model.TodoItem
import org.alexcawl.todoapp.data.repository.TodoItemRepository
import org.alexcawl.todoapp.service.ConverterService
import java.util.*

class ItemViewModel : ViewModel() {
    private val converterService: ConverterService = ConverterService.getInstance()

    private val itemRepository: TodoItemRepository = TodoItemRepository.getInstance()

    val todoItems: MutableLiveData<MutableList<TodoItem>> = MutableLiveData(
        itemRepository.getItems().toMutableList()
    )

    fun getRandomID(): String = UUID.randomUUID().toString()

    fun representTimeUpToDays(calendar: Calendar?): String {
        return when (calendar) {
            null -> ""
            else -> converterService.getUpToDays(calendar)
        }
    }

    fun representTimeUpToMinutes(calendar: Calendar?): String {
        return when (calendar) {
            null -> ""
            else -> converterService.getUpToMinutes(calendar)
        }
    }
}