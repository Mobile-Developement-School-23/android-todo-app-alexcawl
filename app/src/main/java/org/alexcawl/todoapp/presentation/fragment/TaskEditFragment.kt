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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentTaskEditBinding
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.presentation.ToDoApplication
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.model.TaskViewModelFactory
import org.alexcawl.todoapp.presentation.util.*
import java.util.*
import javax.inject.Inject

class TaskEditFragment : Fragment() {
    @Inject
    lateinit var modelFactory: TaskViewModelFactory
    private val model: TaskViewModel by lazy {
        ViewModelProvider(this, modelFactory)[TaskViewModel::class.java]
    }
    private var _binding: FragmentTaskEditBinding? = null
    private val binding: FragmentTaskEditBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (requireContext().applicationContext as ToDoApplication).appComponent.inject(this)
        _binding = FragmentTaskEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        val navController = findNavController()
        try {
            val id: UUID = getID(arguments)
            setupLifecycleData(id, navController)
        } catch (exception: IllegalArgumentException) {
            Snackbar.make(view, exception.message ?: "", Snackbar.LENGTH_LONG).show()
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Throws(IllegalArgumentException::class)
    private fun getID(arguments: Bundle?): UUID {
        val stringID = arguments?.getString("UUID")
            ?: throw IllegalArgumentException("Null UUID!")
        return UUID.fromString(stringID)
            ?: throw IllegalArgumentException("Non-valid UUID: $stringID")
    }

    private fun setupLifecycleData(id: UUID, navController: NavController) {
        lifecycleScope.launch {
            model.requireTask(id).collect { uiState: UiState<TaskModel> ->
                when (uiState) {
                    is UiState.Start -> {}
                    is UiState.Error -> navController.navigateUp().also {
                        binding.root.snackbar(uiState.cause)
                    }
                    is UiState.Success -> with(binding) {
                        setupCloseButton(taskCloseButton, navController)
                        setupSaveButton(taskSaveButton, navController, uiState.data)
                        setupTaskText(taskText, uiState.data)
                        setupTaskPriority(prioritySpinner, taskPriority, uiState.data)
                        setupTaskDeadline(
                            deadlineSwitch,
                            taskDeadline,
                            deadlinePicker,
                            uiState.data
                        )
                        setupTaskDates(taskCreatedAt, taskChangedAt, uiState.data)
                        setupDeleteButton(taskDeleteButton, navController, uiState.data)
                    }
                }
            }
        }
    }

    private fun setupCloseButton(button: AppCompatImageButton, navController: NavController) {
        button.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun setupSaveButton(
        button: AppCompatButton,
        navController: NavController,
        task: TaskModel
    ) {
        button.setOnClickListener {
            lifecycle.coroutineScope.launch {
                model.setTask(task).collect { uiState: UiState<String> ->
                    when (uiState) {
                        is UiState.Start -> {}
                        is UiState.Success -> navController.navigateUp()
                        is UiState.Error -> navController.navigateUp().also {
                            button.snackbar(uiState.cause)
                        }
                    }
                }
            }
        }
    }

    private fun setupDeleteButton(
        button: AppCompatButton, navController: NavController, task: TaskModel
    ) {
        button.setOnClickListener {
            lifecycle.coroutineScope.launch {
                model.removeTask(task).collect { uiState ->
                    when (uiState) {
                        is UiState.Start -> {}
                        is UiState.Success -> navController.navigateUp()
                        is UiState.Error -> navController.navigateUp().also {
                            button.snackbar(uiState.cause)
                        }
                    }
                }
            }
        }
    }

    private fun setupTaskText(
        editText: AppCompatEditText, task: TaskModel
    ) {
        editText.setText(task.text)
        editText.addTextChangedListener {
            task.text = it.toString()
        }
    }

    private fun setupTaskPriority(
        spinner: AppCompatSpinner, textView: AppCompatTextView, task: TaskModel
    ) {
        textView.text = task.priority.toTextFormat()
        spinner.setSelection(
            when (task.priority) {
                Priority.LOW -> 1
                Priority.IMPORTANT -> 2
                else -> 0
            }
        )
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                task.priority = when (position) {
                    0 -> Priority.BASIC
                    1 -> Priority.LOW
                    else -> Priority.IMPORTANT
                }
                textView.text = task.priority.toTextFormat()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupTaskDeadline(
        switch: SwitchCompat,
        textView: AppCompatTextView,
        clickableArea: View,
        task: TaskModel
    ) {
        switch.isChecked = task.deadline != null
        textView.text = task.deadline?.toDateFormat()
            ?: textView.context.getText(R.string.not_defined)
        switch.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    val dateString = createDateString(Calendar.getInstance())
                    task.deadline = task.deadline ?: dateStringToTimestamp(dateString)
                    textView.text = task.deadline?.toDateFormat() ?: dateString
                    clickableArea.isClickable = true
                }
                false -> {
                    textView.text = switch.context.getText(R.string.not_defined)
                    task.deadline = null
                    clickableArea.isClickable = false
                }
            }
        }
        clickableArea.setOnClickListener {
            it.context.createDatePicker { _, year, month, dayOfMonth ->
                val timestamp = createDateString(dayOfMonth, month, year)
                textView.text = timestamp
                task.deadline = dateStringToTimestamp(timestamp)
            }.show()
        }
        clickableArea.isClickable = switch.isChecked
    }

    private fun setupTaskDates(
        textViewCreatedAt: AppCompatTextView,
        textViewChangedAt: AppCompatTextView,
        task: TaskModel
    ) {
        textViewCreatedAt.text = task.creationTime.toDateFormat()
        textViewChangedAt.text = task.modifyingTime.toDateFormat()
    }
}