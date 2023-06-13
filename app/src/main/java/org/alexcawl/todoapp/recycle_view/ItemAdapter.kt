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
            /*
            * Checkbox
            * */
            this.taskCheckbox.isChecked = item.isDone
            when (item.isDone) {
                true -> this.taskCheckbox.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                false -> this.taskCheckbox.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }

            /*
            * Text
            * */
            this.taskCheckbox.text = item.text

            /*
            * Deadline
            * */
            this.taskDeadline.visibility = when (item.deadline) {
                null -> View.GONE
                else -> {
                    this.taskDeadline.text = item.deadline.toString()
                    View.VISIBLE
                }
            }

            /*
            * Icon
            * */
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

            /*
            * CheckBox
            * */
            this.taskCheckbox.setOnClickListener {
                item.isDone = this.taskCheckbox.isChecked
                when (this.taskCheckbox.isChecked) {
                    true -> this.taskCheckbox.apply {
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    false -> this.taskCheckbox.apply {
                        paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }
                }
            }

            /*
            * Check Button
            * */
            this.checkButton.setOnClickListener {
                this.swipeableLayout.close(true)
                this.taskCheckbox.isChecked = !this.taskCheckbox.isChecked
                item.isDone = this.taskCheckbox.isChecked
                when (this.taskCheckbox.isChecked) {
                    true -> this.taskCheckbox.apply {
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    false -> this.taskCheckbox.apply {
                        paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }
                }
            }

            /*
            * Edit Button
            * */
            this.editButton.setOnClickListener {
                this.swipeableLayout.close(true)
                onEditClicked(item)
            }

            /*
            * Edit Button
            * */
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