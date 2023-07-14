package org.alexcawl.todoapp.presentation.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentTaskListBinding
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.presentation.activity.MainActivity
import org.alexcawl.todoapp.presentation.adapter.OnItemSwipeCallback
import org.alexcawl.todoapp.presentation.adapter.TaskItemAdapter
import org.alexcawl.todoapp.presentation.model.MainViewModel
import org.alexcawl.todoapp.presentation.model.SettingsViewModel
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.model.ViewModelFactory
import org.alexcawl.todoapp.presentation.util.UiState
import org.alexcawl.todoapp.presentation.util.invisible
import org.alexcawl.todoapp.presentation.util.snackBar
import org.alexcawl.todoapp.presentation.util.toSnackBarUndoText
import javax.inject.Inject

/**
 * Application main screen with tasks list
 * @see MainViewModel
 * @see TaskViewModel
 * @see SettingsViewModel
 * */
class TaskListFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelFactory
    private val model: MainViewModel by lazy {
        ViewModelProvider(this, modelFactory)[MainViewModel::class.java]
    }

    private var _binding: FragmentTaskListBinding? = null
    private val binding: FragmentTaskListBinding get() = _binding!!

    private val visibility: StateFlow<Boolean> by lazy { model.visibility }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navigationController = findNavController()
        with(binding) {
            syncWhenCreated(binding.root)
            setupActionButton(fab, navigationController)
            setupSettingsButton(settingsButton, navigationController)
            setupRecyclerView(recyclerView, navigationController)
            setupVisibilityButton(visibilityCollapsedButton)
            setupDoneCounter(doneCounter)
            setupUpdateButton(orderCollapsedButton)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun syncWhenCreated(view: View) {
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            model.synchronize().collect {
                when (it) {
                    is UiState.Error -> view.snackBar(it.cause)
                    else -> {}
                }
            }
        }
    }

    private fun setupDoneCounter(view: AppCompatTextView) {
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            model.allTasks.collectLatest { state ->
                view.text = when (state) {
                    is UiState.Success -> resources.getString(
                        R.string.done, state.data.filter { it.isDone }.size
                    )
                    else -> resources.getString(R.string.done, 0)
                }
            }
        }
    }

    private fun setupVisibilityButton(view: AppCompatImageButton) {
        view.setImageDrawable(
            when (visibility.value) {
                true -> ContextCompat.getDrawable(view.context, R.drawable.icon_visibility_on)
                false -> ContextCompat.getDrawable(view.context, R.drawable.icon_visibility_off)
            }
        )

        view.setOnClickListener {
            model.invertVisibilityState()
            view.setImageDrawable(
                when (visibility.value) {
                    true -> ContextCompat.getDrawable(view.context, R.drawable.icon_visibility_on)
                    false -> ContextCompat.getDrawable(view.context, R.drawable.icon_visibility_off)
                }
            )
        }
    }

    private fun setupUpdateButton(view: AppCompatImageButton) {
        val isEnabled = model.isServerEnabled()
        if (isEnabled) {
            view.setOnClickListener {
                lifecycle.coroutineScope.launch(Dispatchers.IO) {
                    model.synchronize().collect {
                        when (it) {
                            is UiState.Success -> view.snackBar("Synchronized!")
                            is UiState.Error -> view.snackBar(it.cause)
                            else -> view.snackBar("Loading...")
                        }
                    }
                }
            }
        } else {
            view.invisible()
        }
    }

    private fun setupActionButton(view: FloatingActionButton, navController: NavController) {
        view.setOnClickListener {
            navController.navigate(R.id.taskAddAction)
        }
    }

    private fun setupSettingsButton(view: AppCompatImageButton, navController: NavController) {
        view.setOnClickListener {
            navController.navigate(R.id.setupSettingsAction)
        }
    }

    private fun setupRecyclerView(view: RecyclerView, navController: NavController) {
        val viewManager = LinearLayoutManager(context)
        val viewAdapter = TaskItemAdapter(
            onEditClicked = { navigateToEdit(navController, it) },
            onInfoClicked = { navigateToShow(navController, it) },
            onTaskSwipeLeft = { onTaskSwipeLeft(view, it) },
            onTaskSwipeRight = { onTaskSwipeRight(view, it) }
        )

        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            visibility.collectLatest { visibilityState ->
                when (visibilityState) {
                    true -> {
                        model.allTasks.collectLatest { uiState ->
                            when (uiState) {
                                is UiState.Success -> viewAdapter.submitList(uiState.data)
                                is UiState.Error -> view.snackBar(uiState.cause)
                                else -> viewAdapter.submitList(listOf())
                            }
                        }
                    }
                    false -> {
                        model.undoneTasks.collectLatest { uiState ->
                            when (uiState) {
                                is UiState.Success -> viewAdapter.submitList(uiState.data)
                                is UiState.Error -> view.snackBar(uiState.cause)
                                else -> viewAdapter.submitList(listOf())
                            }
                        }
                    }
                }
            }
        }
        val swipeHelper = ItemTouchHelper(
            OnItemSwipeCallback({ position -> viewAdapter.onItemSwipeLeft(position) },
                { position -> viewAdapter.onItemSwipeRight(position) },
                ContextCompat.getDrawable(requireContext(), R.drawable.icon_check),
                ColorDrawable(ContextCompat.getColor(requireContext(), R.color.green)),
                ContextCompat.getDrawable(requireContext(), R.drawable.icon_delete),
                ColorDrawable(ContextCompat.getColor(requireContext(), R.color.red)),
                ColorDrawable(ContextCompat.getColor(requireContext(), R.color.white)),
                70,
                20
            )
        )
        with(view) {
            layoutManager = viewManager
            adapter = viewAdapter
            swipeHelper.attachToRecyclerView(view)
        }
    }

    private fun navigateToEdit(navController: NavController, task: TaskModel) {
        navController.navigate(R.id.taskEditAction, bundleOf("UUID" to task.id.toString()))
    }

    private fun navigateToShow(navController: NavController, task: TaskModel) {
        navController.navigate(R.id.taskShowAction, bundleOf("UUID" to task.id.toString()))
    }

    private fun onTaskSwipeLeft(view: View, task: TaskModel) {
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            model.deleteTask(task).collect { uiState ->
                when (uiState) {
                    is UiState.Error -> view.snackBar(uiState.cause)
                    else -> {}
                }
            }
        }
        view.snackBar(
            task.text.toSnackBarUndoText(view.context),
            5000,
            view.context.getString(R.string.undo),
            view.context.getColor(R.color.blue)
        ) {
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                model.updateTask(task).collect { uiState ->
                    when (uiState) {
                        is UiState.Error -> view.snackBar(uiState.cause)
                        else -> {}
                    }
                }
            }
        }
    }

    private fun onTaskSwipeRight(view: View, task: TaskModel) {
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            model.updateTask(task).collect { uiState ->
                when (uiState) {
                    is UiState.Error -> view.snackBar(uiState.cause)
                    else -> {}
                }
            }
        }
    }
}