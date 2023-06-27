package org.alexcawl.todoapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import org.alexcawl.todoapp.databinding.LayoutTaskViewBinding
import org.alexcawl.todoapp.domain.model.TaskModel
import java.util.*

class TaskItemAdapter(
    private val onEditClicked: (TaskModel) -> Unit,
    private val onInfoClicked: (TaskModel) -> Unit,
    val onTaskSwipeLeft: (TaskModel) -> Unit,
    val onTaskSwipeRight: (TaskModel) -> Unit,
    val onTaskDrag: (List<TaskModel>) -> Unit,
) : ListAdapter<TaskModel, TaskItemViewHolder>(TaskItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTaskViewBinding.inflate(inflater, parent, false)
        return TaskItemViewHolder(binding, onEditClicked, onInfoClicked)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    fun onItemSwipeLeft(position: Int) {
        onTaskSwipeLeft(getItem(position))
    }

    fun onItemSwipeRight(position: Int) {
        val task = getItem(position)
        onTaskSwipeRight(task.copy(isDone = !task.isDone))
    }

    fun onItemDrag(fromPosition: Int, toPosition: Int) {
        val tasks = currentList.toMutableList()
        Collections.swap(tasks, fromPosition, toPosition)
        onTaskDrag(tasks)
    }
}