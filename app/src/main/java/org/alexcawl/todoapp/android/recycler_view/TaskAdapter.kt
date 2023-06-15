package org.alexcawl.todoapp.android.recycler_view

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors.getColor
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.data.model.TodoItem
import org.alexcawl.todoapp.databinding.LayoutTaskViewBinding
import org.alexcawl.todoapp.service.ConverterService
import java.util.*

class TaskAdapter(
    private val list: MutableList<TodoItem>,
    private val onEditClicked: (TodoItem) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>(), TaskMovementAdapter {
    private val converterService: ConverterService = ConverterService.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTaskViewBinding.inflate(inflater, parent, false)
        return TaskViewHolder(
            binding,
            getColor(parent, R.attr.activatedColor),
            getColor(parent, R.attr.containerColor)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = list[position]
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

            this.root.setOnClickListener {
                onEditClicked(item)
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
                    TodoItem.Companion.Priority.LOW -> R.drawable.baseline_arrow_downward_24
                    TodoItem.Companion.Priority.HIGH -> R.drawable.baseline_priority_high_24
                    else -> 0
                }, 0, 0, 0
            )
            this.taskContent.compoundDrawables[0]?.setTint(
                when(item.priority) {
                    TodoItem.Companion.Priority.HIGH -> getColor(holder.itemView, R.attr.failureColor)
                    else -> getColor(holder.itemView, R.attr.textSecondaryColor)
                }
            )

            /*
            * Deadline
            * */
            this.taskDeadline.visibility = when (val deadline = item.deadline) {
                null -> View.GONE
                else -> {
                    this.taskDeadline.text = converterService.getUpToDays(deadline)
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

    override fun onItemMoveLeft(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMoveRight(position: Int) {
        list[position].isDone = !list[position].isDone
        notifyItemChanged(position)
    }

    override fun onItemDrag(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}