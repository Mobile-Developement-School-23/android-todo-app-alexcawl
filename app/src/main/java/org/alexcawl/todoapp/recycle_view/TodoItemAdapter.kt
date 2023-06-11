package org.alexcawl.todoapp.recycle_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.alexcawl.todoapp.data.TodoItem
import org.alexcawl.todoapp.databinding.ViewTodoItemBinding
import java.time.LocalDateTime

class TodoItemAdapter : RecyclerView.Adapter<TodoItemViewHolder>() {
    private val todoItems: MutableList<TodoItem> = mutableListOf(
        TodoItem("1", "Task 1", TodoItem.Companion.Priority.LOW, false, LocalDateTime.now()),
        TodoItem("2", "Task 2", TodoItem.Companion.Priority.NORMAL, false, LocalDateTime.now()),
        TodoItem("3", "Task 3", TodoItem.Companion.Priority.HIGH, false, LocalDateTime.now())
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewTodoItemBinding.inflate(inflater, parent, false)
        return TodoItemViewHolder(binding)
    }

    override fun getItemCount(): Int = todoItems.size

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val todoItem = todoItems[position]
        val context = holder.itemView.context

        with(holder.binding) {
            this.textItemContent.text = todoItem.text
            this.textItemDeadline.text = todoItem.creationTime.toString()
        }
    }
}