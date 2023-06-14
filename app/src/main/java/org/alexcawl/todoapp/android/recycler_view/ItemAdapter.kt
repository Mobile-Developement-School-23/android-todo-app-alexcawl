package org.alexcawl.todoapp.android.recycler_view

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.data.TodoItem
import org.alexcawl.todoapp.databinding.LayoutTaskViewBinding
import java.util.*

class ItemAdapter(
    private val list: MutableList<TodoItem>,
    private val onEditClicked: (TodoItem) -> Unit
) : RecyclerView.Adapter<ItemViewHolder>(), ItemTouchHelperAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTaskViewBinding.inflate(inflater, parent, false)
        return ItemViewHolder(
            binding,
            parent.context.getColor(R.color.gray_2),
            parent.context.getColor(R.color.gray_1)
        )
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
                true -> this.taskContent.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                false -> this.taskContent.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }

            this.root.setOnLongClickListener {
                onEditClicked(item)
                true
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
            this.taskContent.compoundDrawables[0]?.setTint(context.getColor(
                when(item.priority) {
                    TodoItem.Companion.Priority.HIGH -> R.color.red
                    else -> R.color.gray_8
                }
            ))

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
            this.taskCheckbox.setOnClickListener {
                item.isDone = this.taskCheckbox.isChecked
                when (this.taskCheckbox.isChecked) {
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
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}