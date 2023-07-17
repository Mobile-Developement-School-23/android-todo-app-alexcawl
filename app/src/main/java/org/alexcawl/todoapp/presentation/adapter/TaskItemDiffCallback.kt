package org.alexcawl.todoapp.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import org.alexcawl.todoapp.domain.model.TaskModel

class TaskItemDiffCallback : DiffUtil.ItemCallback<TaskModel>() {
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
        oldItem == newItem
}