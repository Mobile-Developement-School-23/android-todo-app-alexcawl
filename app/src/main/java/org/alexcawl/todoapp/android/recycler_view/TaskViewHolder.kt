package org.alexcawl.todoapp.android.recycler_view

import androidx.recyclerview.widget.RecyclerView
import org.alexcawl.todoapp.databinding.LayoutTaskViewBinding

class TaskViewHolder(
    val binding: LayoutTaskViewBinding,
    private val colorMoved: Int,
    private val colorDefault: Int
) : RecyclerView.ViewHolder(binding.root), TaskMovementViewHolder {
    override fun onItemSelected() {
        binding.root.setBackgroundColor(colorMoved)
    }

    override fun onItemClear() {
        binding.root.setBackgroundColor(colorDefault)
    }
}