package org.alexcawl.todoapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.databinding.FragmentTaskShowBinding
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.presentation.ToDoApplication
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.model.TaskViewModelFactory
import org.alexcawl.todoapp.presentation.util.*
import java.util.*
import javax.inject.Inject

class TaskShowFragment : Fragment() {
    @Inject
    lateinit var modelFactory: TaskViewModelFactory
    private val model: TaskViewModel by lazy {
        ViewModelProvider(this, modelFactory)[TaskViewModel::class.java]
    }
    private var _binding: FragmentTaskShowBinding? = null
    private val binding: FragmentTaskShowBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (requireContext().applicationContext as ToDoApplication).appComponent.inject(this)
        _binding = FragmentTaskShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        try {
            val id: UUID = getID(arguments)
            setupLifecycleData(id, navController)
        } catch (exception: IllegalArgumentException) {
            view.snackbar("Task ID does not exists!")
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
            model.requireTask(uuid).collect { uiState: UiState<TaskModel> ->
                when (uiState) {
                    is UiState.Start -> {}
                    is UiState.Error -> navController.navigateUp().also {
                        binding.root.snackbar(uiState.cause)
                    }
                    is UiState.Success -> with(binding) {
                        setupCloseButton(taskCloseButton, navController)
                        setupTaskText(taskText, uiState.data)
                        setupTaskPriority(taskPriority, uiState.data)
                        setupTaskDeadline(deadlineBlock, taskDeadline, uiState.data)
                        setupTaskDates(taskCreatedAt, taskChangedAt, uiState.data)
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

    private fun setupTaskText(textView: AppCompatTextView, task: TaskModel) {
        textView.text = task.text
    }

    private fun setupTaskPriority(textView: AppCompatTextView, task: TaskModel) {
        textView.text = task.priority.toTextFormat()
    }

    private fun setupTaskDeadline(block: LinearLayoutCompat , textView: AppCompatTextView, task: TaskModel) {
        when (val deadline = task.deadline) {
            null -> block.invisible()
            else -> textView.text = deadline.toDateFormat()
        }
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