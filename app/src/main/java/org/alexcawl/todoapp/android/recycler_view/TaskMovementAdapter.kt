package org.alexcawl.todoapp.android.recycler_view

import androidx.recyclerview.widget.RecyclerView

interface TaskMovementAdapter {
    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and not at the end of a "drop" event.
     *
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then end position of the moved item.
     * @see RecyclerView.getAdapterPositionFor
     * @see RecyclerView.ViewHolder.getAdapterPosition
     */
    fun onItemDrag(fromPosition: Int, toPosition: Int)


    /**
     * Called when an item has been dismissed by a swipe [<-].
     *
     * @param position The position of the item dismissed.
     * @see RecyclerView.getAdapterPositionFor
     * @see RecyclerView.ViewHolder.getAdapterPosition
     */
    fun onItemMoveLeft(position: Int)

    /**
     * Called when an item has been checked by a swipe [->].
     *
     * @param position The position of the item dismissed.
     * @see RecyclerView.getAdapterPositionFor
     * @see RecyclerView.ViewHolder.getAdapterPosition
     */
    fun onItemMoveRight(position: Int)
}