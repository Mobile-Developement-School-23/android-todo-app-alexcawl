package org.alexcawl.todoapp.spinner_view

import android.view.View
import android.widget.AdapterView

class SpinnerListener(
    private val onItemSelected: (Int) -> Unit = {},
    private val onNothingSelected: () -> Unit = {}
) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onItemSelected(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        onNothingSelected()
    }
}