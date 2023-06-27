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
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.presentation.util.gone
import org.alexcawl.todoapp.presentation.util.toDateFormat

class TaskItemViewHolder(
    private val binding: LayoutTaskViewBinding,
    private val onEditClicked: (TaskModel) -> Unit,
    private val onInfoClicked: (TaskModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(task: TaskModel) {
        with(binding) {
            setupTaskStatus(taskStatus, task)
            setupTaskInfo(taskInfo, task)
            setupTaskText(taskText, task)
            setupTaskDeadline(taskDeadline, task)
            setupTaskEdit(root, task)
        }
    }

    private fun setupTaskStatus(view: AppCompatImageView, task: TaskModel) {
        val taskPriority = task.priority
        val taskStatus = task.isDone
        with(view) {
            setImageDrawable(
                when (taskStatus) {
                    true -> ContextCompat.getDrawable(
                        view.context, R.drawable.icon_checkbox_done
                    )
                    false -> when (taskPriority) {
                        TaskModel.Companion.Priority.HIGH -> ContextCompat.getDrawable(
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

    private fun setupTaskText(view: AppCompatTextView, task: TaskModel) {
        val taskText: String = task.text
        val taskPriority = task.priority
        val taskStatus = task.isDone
        with(view) {
            text = taskText
            compoundDrawables[0] = when (taskPriority) {
                TaskModel.Companion.Priority.HIGH -> ContextCompat.getDrawable(
                    view.context, R.drawable.icon_priority_high
                )
                TaskModel.Companion.Priority.LOW -> ContextCompat.getDrawable(
                    view.context, R.drawable.icon_priority_low
                )
                else -> null
            }
            compoundDrawables.getOrNull(0)?.setTint(
                when (taskPriority) {
                    TaskModel.Companion.Priority.HIGH -> ContextCompat.getColor(
                        view.context, R.color.red
                    )
                    TaskModel.Companion.Priority.LOW -> ContextCompat.getColor(
                        view.context, R.color.gray
                    )
                    else -> ContextCompat.getColor(view.context, R.color.white)
                }
            )
            paintFlags = when (taskStatus) {
                true -> paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                false -> paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }

    private fun setupTaskDeadline(view: AppCompatTextView, task: TaskModel) {
        when (val deadline = task.deadline) {
            null -> view.gone()
            else -> {
                view.text = deadline.toDateFormat()
            }
        }
    }

    private fun setupTaskInfo(view: AppCompatImageButton, task: TaskModel) {
        view.setOnClickListener { onInfoClicked(task) }
    }

    private fun setupTaskEdit(view: View, task: TaskModel) {
        view.setOnClickListener { onEditClicked(task) }
    }
}