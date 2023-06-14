package org.alexcawl.todoapp.android.recycler_view

import androidx.recyclerview.widget.RecyclerView
import org.alexcawl.todoapp.databinding.LayoutTaskViewBinding

class ItemViewHolder(
    val binding: LayoutTaskViewBinding,
    private val colorSelected: Int,
    private val colorDefault: Int
) : RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {
    override fun onItemSelected() {
        binding.root.setBackgroundColor(colorSelected)
    }

    override fun onItemClear() {
        binding.root.setBackgroundColor(colorDefault)
    }
}