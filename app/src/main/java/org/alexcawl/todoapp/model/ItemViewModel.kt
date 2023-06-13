package org.alexcawl.todoapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.alexcawl.todoapp.data.TodoItem
import java.time.LocalDateTime

class ItemViewModel : ViewModel() {
    val ID_NAME: String = "identifier"
    val todoItems: MutableLiveData<MutableList<TodoItem>> = MutableLiveData(mutableListOf())

    fun getRandomID(): String = LocalDateTime.now().toString()
}