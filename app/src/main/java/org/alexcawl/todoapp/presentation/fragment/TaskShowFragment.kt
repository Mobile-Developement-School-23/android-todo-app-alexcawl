package org.alexcawl.todoapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentTaskShowBinding
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.presentation.activity.MainActivity
import org.alexcawl.todoapp.presentation.model.MainViewModel
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.model.ViewModelFactory
import org.alexcawl.todoapp.presentation.util.*
import java.util.*
import javax.inject.Inject

/**
 * Single task info without editing screen
 * @see MainViewModel
 * */
class TaskShowFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelFactory
    private val model: TaskViewModel by lazy {
        ViewModelProvider(this, modelFactory)[TaskViewModel::class.java]
    }
    private var _binding: FragmentTaskShowBinding? = null
    private val binding: FragmentTaskShowBinding get() = _binding!!

    private val text: StateFlow<String> by lazy { model.text }
    private val priority: StateFlow<Priority> by lazy { model.priority }
    private val deadline: StateFlow<Long?> by lazy { model.deadline }
    private val createdAt: StateFlow<Long> by lazy { model.createdAt }
    private val changedAt: StateFlow<Long> by lazy { model.changedAt }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
        _binding = FragmentTaskShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        try {
            val id: UUID = getID(arguments)
            setupLifecycleData(id, navController)
        } catch (exception: IllegalArgumentException) {
            view.snackBar("Task ID does not exists!")
            navController.navigateUp()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    @Throws(IllegalArgumentException::class)
    private fun getID(arguments: Bundle?): UUID {
        val uuid = arguments?.getString("UUID")
            ?: throw IllegalArgumentException("Null UUID!")
        return UUID.fromString(uuid)
            ?: throw IllegalArgumentException("Non-valid UUID: $uuid")
    }

    private fun setupLifecycleData(uuid: UUID, navController: NavController) {
        lifecycleScope.launch {
            model.loadTask(uuid).collect { uiState ->
                when (uiState) {
                    is UiState.Error -> navController.navigateUp().also {
                        binding.root.snackBar(uiState.cause)
                    }
                    is UiState.OK -> with(binding) {
                        setupCloseButton(closeButton, navController)
                        setupTaskText(taskText)
                        setupTaskPriority(taskPriority)
                        setupTaskDeadline(taskDeadline)
                        setupTaskDates(taskCreatedAt, taskChangedAt)
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

    private fun setupTaskText(textView: AppCompatTextView) {
        lifecycle.coroutineScope.launch {
            text.collect {
                textView.text = it
            }
        }
    }

    private fun setupTaskPriority(textView: AppCompatTextView) {
        lifecycle.coroutineScope.launch {
            priority.collect {
                textView.text = it.toTextFormat(textView.context)
            }
        }
    }

    private fun setupTaskDeadline(
        textView: AppCompatTextView,
    ) {
        lifecycle.coroutineScope.launch {
            deadline.collectLatest {
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
}