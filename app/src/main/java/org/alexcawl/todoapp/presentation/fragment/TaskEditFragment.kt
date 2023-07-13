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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentTaskEditBinding
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.presentation.activity.MainActivity
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.model.MainViewModel
import org.alexcawl.todoapp.presentation.model.ViewModelFactory
import org.alexcawl.todoapp.presentation.util.*
import java.util.*
import javax.inject.Inject

/**
 * Single task info with editing screen
 * @see MainViewModel
 * */
class TaskEditFragment : Fragment(), PriorityDialogFragment.Listener {
    @Inject
    lateinit var modelFactory: ViewModelFactory
    private val model: TaskViewModel by lazy {
        ViewModelProvider(this, modelFactory)[TaskViewModel::class.java]
    }
    private var _binding: FragmentTaskEditBinding? = null
    private val binding: FragmentTaskEditBinding
        get() = _binding!!

    private val text: StateFlow<String> by lazy { model.text }
    private val priority: StateFlow<Priority> by lazy { model.priority }
    private val deadline: StateFlow<Long?> by lazy { model.deadline }
    private val createdAt: StateFlow<Long> by lazy { model.createdAt }
    private val changedAt: StateFlow<Long> by lazy { model.changedAt }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
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
            model.loadTask(id).collect { uiState ->
                when (uiState) {
                    is UiState.Error -> navController.navigateUp().also {
                        binding.root.snackbar(uiState.cause)
                    }
                    is UiState.OK -> with(binding) {
                        setupCloseButton(closeButton, navController)
                        setupSaveButton(saveButton, navController)
                        setupTaskText(taskText)
                        setupPriorityPicker(taskPriority, priorityBlock)
                        setupTaskDeadline(deadlineSwitch, taskDeadline, deadlinePicker)
                        setupTaskDates(taskCreatedAt, taskChangedAt)
                        setupDeleteButton(taskDeleteButton, navController)
                    }
                    else -> {}
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
    ) {
        button.setOnClickListener {
            lifecycle.coroutineScope.launch {
                model.update().collect { uiState ->
                    when (uiState) {
                        is UiState.OK -> navController.navigateUp()
                        is UiState.Error -> navController.navigateUp().also {
                            button.snackbar(uiState.cause)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupDeleteButton(
        button: AppCompatButton, navController: NavController
    ) {
        button.setOnClickListener {
            lifecycle.coroutineScope.launch {
                model.delete().collect { uiState ->
                    when (uiState) {
                        is UiState.OK -> navController.navigateUp()
                        is UiState.Error -> navController.navigateUp().also {
                            button.snackbar(uiState.cause)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupTaskText(
        editText: AppCompatEditText
    ) {
        lifecycle.coroutineScope.launch {
            editText.setText(text.value)
        }
        editText.addTextChangedListener {
            model.setTaskText(it.toString())
        }
    }

    private fun setupPriorityPicker(textView: AppCompatTextView, clickableArea: View) {
        lifecycle.coroutineScope.launch {
            priority.collectLatest {
                textView.text = it.toTextFormat(textView.context)
            }
        }
        clickableArea.setOnClickListener {
            PriorityDialogFragment(priority.value, this).show(parentFragmentManager, "PRIORITY-DIALOG")
        }
    }

    private fun setupTaskDeadline(
        switch: SwitchCompat,
        textView: AppCompatTextView,
        clickableArea: View,
    ) {
        switch.isChecked = deadline.value != null
        switch.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    val dateString = createDateString(Calendar.getInstance())
                    model.setTaskDeadline(dateStringToTimestamp(dateString))
                }
                false -> {
                    model.setTaskDeadline(null)
                }
            }
        }
        clickableArea.setOnClickListener {
            it.context.pickDateAndTime { date ->
                val dateString = createDateString(date)
                textView.text = dateString
                model.setTaskDeadline(dateStringToTimestamp(dateString))
            }
        }
        lifecycle.coroutineScope.launch {
            deadline.collectLatest {
                clickableArea.isClickable = it != null
                textView.text = it?.toDateFormat() ?: textView.context.getText(R.string.not_defined)
            }
        }
    }

    private fun setupTaskDates(
        textViewCreatedAt: AppCompatTextView,
        textViewChangedAt: AppCompatTextView,
    ) {
        lifecycle.coroutineScope.launch {
            createdAt.collectLatest {
                textViewCreatedAt.text = it.toDateFormat()
            }
        }
        lifecycle.coroutineScope.launch {
            changedAt.collectLatest {
                textViewChangedAt.text = it.toDateFormat()
            }
        }
    }

    override fun onSubmit(priority: Priority) = model.setTaskPriority(priority)
}