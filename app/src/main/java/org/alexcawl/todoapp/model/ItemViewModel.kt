package org.alexcawl.todoapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.alexcawl.todoapp.data.TodoItem
import java.time.LocalDateTime

class ItemViewModel : ViewModel() {
    val items: MutableLiveData<MutableList<TodoItem>> = MutableLiveData(mutableListOf())

    fun getRandomID(): String = LocalDateTime.now().toString()

    companion object {
        const val ID_NAME: String = "identifier"
    }
}