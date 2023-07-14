package org.alexcawl.todoapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentTaskAddBinding
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.presentation.activity.MainActivity
import org.alexcawl.todoapp.presentation.model.MainViewModel
import org.alexcawl.todoapp.presentation.model.NewTaskViewModel
import org.alexcawl.todoapp.presentation.model.ViewModelFactory
import org.alexcawl.todoapp.presentation.util.*
import java.util.*
import javax.inject.Inject

/**
 * Task adding screen
 * @see MainViewModel
 * */
class TaskAddFragment : Fragment(), PriorityDialogFragment.Listener {
    @Inject
    lateinit var modelFactory: ViewModelFactory
    private val model: NewTaskViewModel by lazy {
        ViewModelProvider(this, modelFactory)[NewTaskViewModel::class.java]
    }
    private var _binding: FragmentTaskAddBinding? = null
    private val binding: FragmentTaskAddBinding
        get() = _binding!!

    private val priority: StateFlow<Priority> by lazy { model.priority }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
        _binding = FragmentTaskAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        with(binding) {
            setupCloseButton(closeButton, navController)
            setupAddButton(saveButton, navController)
            setupContentScriber(taskText)
            setupPriorityPicker(taskPriority, priorityBlock)
            setupDeadlinePicker(deadlineSwitch, taskDeadline, deadlinePicker)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCloseButton(button: AppCompatImageButton, navController: NavController) {
        button.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun setupAddButton(button: AppCompatButton, navController: NavController) {
        button.setOnClickListener {
            lifecycle.coroutineScope.launch {
                model.addTask().collect { uiState ->
                    when (uiState) {
                        is UiState.OK -> navController.navigateUp()
                        is UiState.Error -> {
                            button.snackBar(uiState.cause)
                            navController.navigateUp()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupContentScriber(editText: AppCompatEditText) {
        editText.addTextChangedListener { text ->
            model.setTaskText(text.toString())
        }
    }

    private fun setupPriorityPicker(textView: AppCompatTextView, clickableArea: View) {
        lifecycle.coroutineScope.launch {
            priority.collect {
                textView.text = it.toTextFormat(textView.context)
            }
        }
        clickableArea.setOnClickListener {
            PriorityDialogFragment(priority.value, this).show(parentFragmentManager, "PRIORITY-DIALOG")
        }
    }

    private fun setupDeadlinePicker(
        switch: SwitchCompat, textView: AppCompatTextView, clickableArea: View
    ) {
        model.setTaskDeadline(null)
        textView.text = switch.context.getText(R.string.not_defined)
        switch.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    val dateString = createDateString(Calendar.getInstance())
                    model.setTaskDeadline(dateStringToTimestamp(dateString))
                    textView.text = dateString
                    clickableArea.isClickable = true
                }
                false -> {
                    textView.text = switch.context.getText(R.string.not_defined)
                    model.setTaskDeadline(null)
                    clickableArea.isClickable = false
                }
            }
        }
        clickableArea.setOnClickListener {
            it.context.pickDateAndTime { date ->
                val timestamp = createDateString(date)
                textView.text = timestamp
                model.setTaskDeadline(dateStringToTimestamp(timestamp))
            }
        }
        clickableArea.isClickable = false
    }

    override fun onSubmit(priority: Priority) = model.setTaskPriority(priority)
}