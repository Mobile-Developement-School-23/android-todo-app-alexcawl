package org.alexcawl.todoapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.alexcawl.todoapp.data.TodoItem

class TodoViewModel() : ViewModel() {
    val addItemText: MutableLiveData<String> = MutableLiveData(null)
    val addItemType: MutableLiveData<TodoItem.Companion.Priority> = MutableLiveData(null)


}