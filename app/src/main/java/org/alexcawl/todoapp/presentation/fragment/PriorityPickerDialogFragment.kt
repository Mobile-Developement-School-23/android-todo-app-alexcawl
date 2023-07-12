package org.alexcawl.todoapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentSheetPriorityPickerBinding
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.presentation.activity.MainActivity
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.model.ViewModelFactory
import org.alexcawl.todoapp.presentation.util.toTextFormat
import javax.inject.Inject

class PriorityPickerDialogFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var modelFactory: ViewModelFactory
    private val model: TaskViewModel by lazy {
        ViewModelProvider(requireActivity(), modelFactory)[TaskViewModel::class.java]
    }

    private val priority: StateFlow<Priority> by lazy { model.priority }

    private var _binding: FragmentSheetPriorityPickerBinding? = null
    private val binding: FragmentSheetPriorityPickerBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
        _binding = FragmentSheetPriorityPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            lifecycle.coroutineScope.launch {
                priority.collect {
                    when(it) {
                        Priority.LOW -> buttonToggleGroup.check(R.id.low_priority_button)
                        Priority.BASIC -> buttonToggleGroup.check(R.id.basic_priority_button)
                        Priority.IMPORTANT -> buttonToggleGroup.check(R.id.hight_priority_button)
                    }
                }
            }
            buttonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.low_priority_button -> {
                            model.setTaskPriority(Priority.LOW)
                            priorityText.text = Priority.LOW.toTextFormat()
                        }
                        R.id.hight_priority_button -> {
                            model.setTaskPriority(Priority.IMPORTANT)
                            priorityText.text = Priority.IMPORTANT.toTextFormat()
                        }
                        else -> {
                            model.setTaskPriority(Priority.BASIC)
                            priorityText.text = Priority.BASIC.toTextFormat()
                        }
                    }
                }
            }
            selectButton.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}