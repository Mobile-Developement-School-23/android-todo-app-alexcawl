package org.alexcawl.todoapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentSheetPriorityPickerBinding
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.presentation.util.toTextFormat

class PriorityDialogFragment(
    startPriority: Priority,
    private val listener: Listener
) : BottomSheetDialogFragment() {
    private var priority: Priority = startPriority
    private var _binding: FragmentSheetPriorityPickerBinding? = null
    private val binding: FragmentSheetPriorityPickerBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSheetPriorityPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            priorityText.text = priority.toTextFormat(view.context)
            when(priority) {
                Priority.LOW -> buttonToggleGroup.check(R.id.low_priority_button)
                Priority.BASIC -> buttonToggleGroup.check(R.id.basic_priority_button)
                Priority.IMPORTANT -> buttonToggleGroup.check(R.id.hight_priority_button)
            }
            buttonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    priority = when (checkedId) {
                        R.id.low_priority_button -> Priority.LOW
                        R.id.hight_priority_button -> Priority.IMPORTANT
                        else -> Priority.BASIC
                    }
                    priorityText.text = priority.toTextFormat(view.context)
                }
            }
            selectButton.setOnClickListener {
                listener.onSubmit(priority)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface Listener {
        fun onSubmit(priority: Priority)
    }
}