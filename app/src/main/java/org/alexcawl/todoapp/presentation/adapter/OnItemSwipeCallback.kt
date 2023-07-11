package org.alexcawl.todoapp.presentation.adapter

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.alexcawl.todoapp.presentation.util.fromDpToPx

class OnItemSwipeCallback(
    private val onSwipeLeft: (Int) -> Unit,
    private val onSwipeRight: (Int) -> Unit,
    private val iconLeft: Drawable? = null,
    private val backgroundLeft: ColorDrawable? = null,
    private val iconRight: Drawable? = null,
    private val backgroundRight: ColorDrawable? = null,
    private val iconTint: ColorDrawable? = null,
    private val iconSizeInDp: Int? = null,
    private val marginInDp: Int? = null
) : ItemTouchHelper.Callback() {
    override fun isLongPressDragEnabled(): Boolean = false

    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position: Int = viewHolder.adapterPosition
        when(direction) {
            ItemTouchHelper.LEFT -> onSwipeLeft(position)
            ItemTouchHelper.RIGHT -> onSwipeRight(position)
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val view = viewHolder.itemView
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val height = view.bottom - view.top
            val iconSize: Int = iconSizeInDp ?: (height / 4)
            val marginVertical: Int = (height - iconSize) / 2
            val marginHorizontal: Int = marginInDp?.fromDpToPx(view.context) ?: marginVertical
            when {
                dX > 0 -> {
                    backgroundLeft?.setBounds(
                        view.left,
                        view.top,
                        view.left + dX.toInt(),
                        view.bottom
                    )
                    if (iconTint != null) { iconLeft?.setTint(iconTint.color) }
                    iconLeft?.setBounds(
                        view.left + marginHorizontal,
                        view.top + marginVertical,
                        view.left + marginHorizontal + iconSize,
                        view.top + marginVertical + iconSize
                    )
                    backgroundLeft?.draw(canvas)
                    iconLeft?.draw(canvas)
                }
                dX < 0 -> {
                    backgroundRight?.setBounds(
                        view.right + dX.toInt(),
                        view.top,
                        view.right,
                        view.bottom
                    )
                    if (iconTint != null) { iconRight?.setTint(iconTint.color) }
                    iconRight?.setBounds(
                        view.right - marginHorizontal - iconSize,
                        view.top + marginVertical,
                        view.right - marginHorizontal,
                        view.top + marginVertical + iconSize
                    )
                    backgroundRight?.draw(canvas)
                    iconRight?.draw(canvas)
                }
            }
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}