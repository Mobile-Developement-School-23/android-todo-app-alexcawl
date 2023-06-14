package org.alexcawl.todoapp.android.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.alexcawl.todoapp.data.model.TodoItem
import org.alexcawl.todoapp.data.repository.TodoItemRepository
import org.alexcawl.todoapp.service.ConverterService
import java.time.LocalDateTime

class ItemViewModel : ViewModel() {
    private val converterService: ConverterService = ConverterService.getInstance()

    private val itemRepository: TodoItemRepository = TodoItemRepository.getInstance()

    val todoItems: MutableLiveData<MutableList<TodoItem>> = MutableLiveData(
        itemRepository.getItems().toMutableList()
    )

    fun getRandomID(): String = LocalDateTime.now().toString()

}