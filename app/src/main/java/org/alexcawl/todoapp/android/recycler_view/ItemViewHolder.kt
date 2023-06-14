package org.alexcawl.todoapp.android.recycler_view

import androidx.recyclerview.widget.RecyclerView
import org.alexcawl.todoapp.databinding.LayoutTaskViewBinding

class ItemViewHolder(
    val binding: LayoutTaskViewBinding
) : RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {
    override fun onItemSelected() {
        // TODO decoration
    }

    override fun onItemClear() {
        // TODO decoration
    }

}