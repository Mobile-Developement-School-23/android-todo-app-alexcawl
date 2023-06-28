package org.alexcawl.todoapp.presentation.adapter

import android.graphics.Paint
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.LayoutTaskViewBinding
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.presentation.util.gone
import org.alexcawl.todoapp.presentation.util.toDateFormat

class TaskItemViewHolder(
    private val binding: LayoutTaskViewBinding,
    private val onEditClicked: (TaskModel) -> Unit,
    private val onInfoClicked: (TaskModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var _task: TaskModel? = null
    val task: TaskModel
        get() = _task!!

    fun onBind(task: TaskModel) {
        _task = task
        with(binding) {
            setupTaskStatus(taskStatus)
            setupTaskInfo(taskInfo)
            setupTaskText(taskText)
            setupTaskDeadline(taskDeadline)
            setupTaskEdit(root)
        }
    }

    private fun setupTaskStatus(view: AppCompatImageView) {
        val taskPriority = task.priority
        val taskStatus = task.isDone
        with(view) {
            setImageDrawable(
                when (taskStatus) {
                    true -> ContextCompat.getDrawable(
                        view.context, R.drawable.icon_checkbox_done
                    )
                    false -> when (taskPriority) {
                        Priority.IMPORTANT -> ContextCompat.getDrawable(
                            view.context, R.drawable.icon_checkbox_unchecked_high
                        )
                        else -> ContextCompat.getDrawable(
                            view.context, R.drawable.icon_checkbox_unchecked
                        )
                    }
                }
            )
        }
    }

    private fun setupTaskText(view: AppCompatTextView) {
        with(view) {
            text = task.text
            val drawable = when (task.priority) {
                Priority.IMPORTANT -> ContextCompat.getDrawable(
                    view.context, R.drawable.icon_priority_high
                )
                Priority.LOW -> ContextCompat.getDrawable(
                    view.context, R.drawable.icon_priority_low
                )
                else -> null
            }?.apply {
                setTint(when (task.priority) {
                    Priority.IMPORTANT -> ContextCompat.getColor(
                        view.context, R.color.red
                    )
                    Priority.LOW -> ContextCompat.getColor(
                        view.context, R.color.gray
                    )
                    else -> ContextCompat.getColor(view.context, R.color.white)
                })
            }
            setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            paintFlags = when (task.isDone) {
                true -> paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                false -> paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }

    private fun setupTaskDeadline(view: AppCompatTextView) {
        when (val deadline = task.deadline) {
            null -> view.gone()
            else -> {
                view.text = deadline.toDateFormat()
            }
        }
    }

    private fun setupTaskInfo(view: AppCompatImageButton) {
        view.setOnClickListener { onInfoClicked(task) }
    }

    private fun setupTaskEdit(view: View) {
        view.setOnClickListener { onEditClicked(task) }
    }
}