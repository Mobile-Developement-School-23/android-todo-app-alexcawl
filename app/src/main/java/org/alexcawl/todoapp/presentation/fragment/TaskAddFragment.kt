package org.alexcawl.todoapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentTaskAddBinding
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.presentation.ToDoApplication
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.model.TaskViewModelFactory
import org.alexcawl.todoapp.presentation.util.*
import java.util.*
import javax.inject.Inject

class TaskAddFragment : Fragment() {
    @Inject
    lateinit var modelFactory: TaskViewModelFactory
    private val model: TaskViewModel by lazy {
        ViewModelProvider(this, modelFactory)[TaskViewModel::class.java]
    }
    private var _binding: FragmentTaskAddBinding? = null
    private val binding: FragmentTaskAddBinding
        get() = _binding!!

    private var textFieldValue: String = ""
    private var priorityFieldValue: Priority = Priority.BASIC
    private var deadlineFieldValue: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireContext().applicationContext as ToDoApplication).appComponent.inject(this)
        _binding = FragmentTaskAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        with(binding) {
            setupCloseButton(taskCloseButton, navController)
            setupAddButton(taskSaveButton, navController)
            setupContentScriber(taskText)
            setupPriorityPicker(prioritySpinner, taskPriority)
            setupDeadlinePicker(deadlineSwitch, taskDeadline, deadlinePicker)
        }
    }

    private fun setupCloseButton(button: AppCompatImageButton, navController: NavController) {
        button.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun setupAddButton(button: AppCompatButton, navController: NavController) {
        button.setOnClickListener {
            lifecycle.coroutineScope.launch {
                model.addTask(textFieldValue, priorityFieldValue, deadlineFieldValue).collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> navController.navigateUp()
                        is UiState.Error -> {
                            button.snackbar(uiState.cause)
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
            textFieldValue = text.toString()
        }
    }

    private fun setupPriorityPicker(spinner: AppCompatSpinner, textView: AppCompatTextView) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                priorityFieldValue = when (position) {
                    0 -> Priority.BASIC
                    1 -> Priority.LOW
                    else -> Priority.IMPORTANT
                }
                textView.text = priorityFieldValue.toString().lowercase(Locale.ROOT)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                priorityFieldValue = Priority.BASIC
                textView.text = priorityFieldValue.toString().lowercase(Locale.ROOT)
            }
        }
    }

    private fun setupDeadlinePicker(
        switch: SwitchCompat,
        textView: AppCompatTextView,
        clickableArea: View
    ) {
        textView.text = switch.context.getText(R.string.not_defined)
        switch.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked) {
                true -> {
                    val dateString = createDateString(Calendar.getInstance())
                    deadlineFieldValue = dateStringToTimestamp(dateString)
                    textView.text = dateString
                    clickableArea.isClickable = true
                }
                false -> {
                    textView.text = switch.context.getText(R.string.not_defined)
                    deadlineFieldValue = null
                    clickableArea.isClickable = false
                }
            }
        }
        clickableArea.setOnClickListener {
            it.context.createDatePicker { _, year, month, dayOfMonth ->
                val timestamp = createDateString(dayOfMonth, month, year)
                textView.text = timestamp
                deadlineFieldValue = dateStringToTimestamp(timestamp)
            }.show()
        }
        clickableArea.isClickable = false
    }
}