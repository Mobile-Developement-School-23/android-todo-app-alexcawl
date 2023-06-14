package org.alexcawl.todoapp.android.recycler_view

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.data.TodoItem
import org.alexcawl.todoapp.databinding.LayoutTaskViewBinding

class ItemAdapter(
    private val list: MutableList<TodoItem>
) : RecyclerView.Adapter<ItemViewHolder>(), ItemTouchHelperAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTaskViewBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            /*
            * Checkbox
            * */
            this.taskContent.isChecked = item.isDone
            when (item.isDone) {
                true -> this.taskContent.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                false -> this.taskContent.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }

            /*
            * Text
            * */
            this.taskContent.text = item.text

            /*
            * Icon
            * */
            this.taskContent.setCompoundDrawablesWithIntrinsicBounds(
                when (item.priority) {
                    TodoItem.Companion.Priority.LOW -> R.drawable.baseline_low_priority_24
                    TodoItem.Companion.Priority.HIGH -> R.drawable.baseline_priority_high_24
                    else -> 0
                }, 0, 0, 0
            )

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
            * CheckBox
            * */
            this.taskContent.setOnClickListener {
                item.isDone = this.taskContent.isChecked
                when (this.taskContent.isChecked) {
                    true -> this.taskContent.apply {
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    false -> this.taskContent.apply {
                        paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }
                }
            }
        }
    }

    override fun onItemRemove(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemCheck(position: Int) {
        list[position].isDone = !list[position].isDone
        notifyItemChanged(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val prev = list.removeAt(fromPosition)
        list.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, prev)
        notifyItemMoved(fromPosition, toPosition)
    }
}