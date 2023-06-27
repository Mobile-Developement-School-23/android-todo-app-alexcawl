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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentTaskEditBinding
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.util.*
import java.util.*

class TaskEditFragment : Fragment() {
    private val model: TaskViewModel by lazy {
        ViewModelProvider(this.requireActivity())[TaskViewModel::class.java]
    }
    private var _binding: FragmentTaskEditBinding? = null
    private val binding: FragmentTaskEditBinding
        get() = _binding!!

    private var _task: TaskModel? = null
    private val task: TaskModel
        get() = _task!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val navController = findNavController()
        try {
            val (id: UUID, isEnabled: Boolean) = getID(arguments)
            lifecycle.coroutineScope.launch {
                model.getAllTasks().collect { list ->
                    _task = list.find { task -> task.id == id }!!

                    with(binding) {
                        setupCloseButton(taskCloseButton, navController)
                        setupSaveButton(taskSaveButton, navController, isEnabled)
                        setupDeleteButton(taskDeleteButton, navController, isEnabled)
                        setupContentScriber(taskContentText, isEnabled)
                        setupPriorityPicker(taskPrioritySpinner, taskPriorityText, isEnabled)
                        setupDeadlinePicker(
                            taskDeadlineSwitch,
                            taskDeadlineText,
                            taskDeadlinePickerArea,
                            isEnabled
                        )
                        setupDateInfo(taskCreationTimeText, taskModifyingTimeText, taskModifyingTimeArea)
                    }
                }
            }
        } catch (exception: IllegalArgumentException) {
            Snackbar.make(view, exception.message ?: "", Snackbar.LENGTH_LONG).show()
            navController.navigateUp()
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getID(arguments: Bundle?): Pair<UUID, Boolean> {
        val stringID = arguments?.getString(TodoApplication.UUID)
            ?: throw IllegalArgumentException("Null UUID!")
        val uuid = UUID.fromString(stringID)
            ?: throw IllegalArgumentException("Non-valid UUID: $stringID")
        val isEnabled = arguments.getBoolean(TodoApplication.isEnabled)
        return Pair(uuid, isEnabled)
    }

    private fun setupCloseButton(button: AppCompatImageButton, navController: NavController) {
        button.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun setupSaveButton(
        button: AppCompatButton,
        navController: NavController,
        isEnabled: Boolean
    ) {
        when (isEnabled) {
            true -> button.setOnClickListener {
                lifecycle.coroutineScope.launch {
                    model.setTask(task)
                    navController.navigateUp()
                }
            }
            false -> {
                button.disable()
                button.text = ""
            }
        }
    }

    private fun setupDeleteButton(
        button: AppCompatButton,
        navController: NavController,
        isEnabled: Boolean
    ) {
        when (isEnabled) {
            true -> button.setOnClickListener {
                lifecycle.coroutineScope.launch {
                    model.removeTask(task)
                    navController.navigateUp()
                }
            }
            false -> button.invisible()
        }
    }

    private fun setupContentScriber(editText: AppCompatEditText, isEnabled: Boolean) {
        editText.setText(task.text)
        when (isEnabled) {
            true -> editText.addTextChangedListener { task.text = it.toString() }
            false -> editText.disable()
        }
    }

    private fun setupPriorityPicker(
        spinner: AppCompatSpinner,
        textView: AppCompatTextView,
        isEnabled: Boolean
    ) {
        when (isEnabled) {
            true -> spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    task.priority = when (position) {
                        0 -> TaskModel.Companion.Priority.NORMAL
                        1 -> TaskModel.Companion.Priority.LOW
                        else -> TaskModel.Companion.Priority.HIGH
                    }
                    textView.text = task.priority.toString().lowercase(Locale.ROOT)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            false -> {
                textView.text = task.priority.toString().lowercase(Locale.ROOT)
                spinner.gone()
            }
        }
    }

    private fun setupDeadlinePicker(
        switch: SwitchCompat,
        textView: AppCompatTextView,
        clickableArea: View,
        isEnabled: Boolean
    ) {
        textView.text = task.deadline?.toDateFormat() ?: textView.context.getText(R.string.not_defined)
        when (isEnabled) {
            true -> {
                switch.setOnCheckedChangeListener { _, isChecked ->
                    when (isChecked) {
                        true -> {
                            val dateString = createDateString(Calendar.getInstance())
                            task.deadline = task.deadline ?: dateStringToTimestamp(dateString)
                            textView.text =
                                task.deadline?.toDateFormat() ?: dateString
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
                clickableArea.isClickable = false
            }
            false -> switch.gone()
        }
    }

    private fun setupDateInfo(
        creationDateTextView: AppCompatTextView,
        modifyingDateTextView: AppCompatTextView,
        modifyingDateArea: View
    ) {
        val creationDate = task.creationTime
        val modifyingDate = task.modifyingTime

        creationDateTextView.text = creationDate.toDateFormat()
        when (modifyingDate) {
            null -> modifyingDateArea.gone()
            else -> modifyingDateTextView.text =
                modifyingDate.toDateFormat()
        }
    }
}