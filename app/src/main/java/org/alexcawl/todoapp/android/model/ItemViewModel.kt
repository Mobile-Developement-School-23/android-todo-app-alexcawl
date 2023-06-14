package org.alexcawl.todoapp.android.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.alexcawl.todoapp.data.TodoItem
import java.time.LocalDateTime

class ItemViewModel : ViewModel() {
    val todoItems: MutableLiveData<MutableList<TodoItem>> = MutableLiveData(mutableListOf())

    fun getRandomID(): String = LocalDateTime.now().toString()

}