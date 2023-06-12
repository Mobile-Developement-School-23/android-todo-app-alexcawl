package org.alexcawl.todoapp.recycle_view

import android.graphics.Paint
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.data.TodoItem
import org.alexcawl.todoapp.databinding.LayoutTodoItemBinding

class ItemAdapter(
    private val list: MutableList<TodoItem>, private val onEditClicked: (TodoItem) -> Unit
) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTodoItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        val context = holder.itemView.context
        with(holder.binding) {
            this.contentTextview.text = item.text
            this.deadlineTextview.visibility = when (item.deadline) {
                null -> View.GONE
                else -> {
                    this.deadlineTextview.text = item.deadline.toString()
                    View.VISIBLE
                }
            }
            this.priorityIcon.setImageIcon(
                when (item.priority) {
                    TodoItem.Companion.Priority.LOW -> Icon.createWithResource(
                        context, R.drawable.baseline_low_priority_24
                    )
                    TodoItem.Companion.Priority.NORMAL -> Icon.createWithResource(
                        context, R.drawable.baseline_square_24
                    )
                    TodoItem.Companion.Priority.HIGH -> Icon.createWithResource(
                        context, R.drawable.baseline_priority_high_24
                    )
                }
            )
            this.checkButton.setOnClickListener {
                this.swipeableLayout.close(true)
                this.statusCheckbox.isChecked = !this.statusCheckbox.isChecked
                when (this.statusCheckbox.isChecked) {
                    true -> this.contentTextview.apply {
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    false -> this.contentTextview.apply {
                        paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }
                }
            }
            this.editButton.setOnClickListener {
                this.swipeableLayout.close(true)
                onEditClicked(item)
            }
            this.deleteButton.setOnClickListener {
                this.swipeableLayout.close(true)
                removeItemAt(position)
            }
        }
    }

    private fun addItem(item: TodoItem) {
        list.add(item)
        notifyItemInserted(list.size - 1)
    }

    private fun replaceItemAt(item: TodoItem, position: Int) {
        list[position] = item
        notifyItemChanged(position)
    }

    private fun removeItemAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }
}