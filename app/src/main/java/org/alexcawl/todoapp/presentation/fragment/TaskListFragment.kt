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
import org.alexcawl.todoapp.presentation.activity.MainActivity
import org.alexcawl.todoapp.presentation.adapter.OnItemSwipeCallback
import org.alexcawl.todoapp.presentation.adapter.TaskItemAdapter
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.model.TaskViewModelFactory
import org.alexcawl.todoapp.presentation.util.UiState
import org.alexcawl.todoapp.presentation.util.snackbar
import javax.inject.Inject

/**
 * Application main screen with tasks list
 * @see TaskViewModel
 * */
class TaskListFragment : Fragment() {
    @Inject
    lateinit var modelFactory: TaskViewModelFactory
    private val model: TaskViewModel by lazy {
        ViewModelProvider(this, modelFactory)[TaskViewModel::class.java]
    }
    private val visibility: StateFlow<Boolean> by lazy { model.visibility }
    private var _binding: FragmentTaskListBinding? = null
    private val binding: FragmentTaskListBinding get() = _binding!!

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
                    is UiState.Error -> view.snackbar(it.cause)
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
        view.setOnClickListener {
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                model.synchronize().collect {
                    when (it) {
                        is UiState.Start -> view.snackbar("Loading...")
                        is UiState.Success -> view.snackbar("Synchronized!")
                        is UiState.Error -> view.snackbar(it.cause)
                    }
                }
            }
        }
    }

    private fun setupActionButton(view: FloatingActionButton, navController: NavController) {
        view.setOnClickListener {
            navController.navigate(R.id.taskAddAction)
        }
    }

    private fun setupRecyclerView(view: RecyclerView, navController: NavController) {
        val viewManager = LinearLayoutManager(context)
        val viewAdapter = TaskItemAdapter(onEditClicked = {
            navController.navigate(R.id.taskEditAction, bundleOf("UUID" to it.id.toString()))
        }, onInfoClicked = {
            navController.navigate(R.id.taskShowAction, bundleOf("UUID" to it.id.toString()))
        }, onTaskSwipeLeft = {
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                model.removeTask(it).collect { uiState ->
                    when (uiState) {
                        is UiState.Error -> view.snackbar(uiState.cause)
                        else -> {}
                    }
                }
            }
        }, onTaskSwipeRight = {
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                model.setTask(it).collect { uiState ->
                    when (uiState) {
                        is UiState.Error -> view.snackbar(uiState.cause)
                        else -> {}
                    }
                }
            }
        })

        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            visibility.collectLatest { visibilityState ->
                when (visibilityState) {
                    true -> {
                        model.allTasks.collectLatest { uiState ->
                            when (uiState) {
                                is UiState.Success -> viewAdapter.submitList(uiState.data)
                                is UiState.Error -> view.snackbar(uiState.cause)
                                is UiState.Start -> viewAdapter.submitList(listOf())
                            }
                        }
                    }
                    false -> {
                        model.undoneTasks.collectLatest { uiState ->
                            when (uiState) {
                                is UiState.Success -> viewAdapter.submitList(uiState.data)
                                is UiState.Error -> view.snackbar(uiState.cause)
                                is UiState.Start -> viewAdapter.submitList(listOf())
                            }
                        }
                    }
                }
            }
        }
        val swipeHelper = ItemTouchHelper(
            OnItemSwipeCallback(
                { position -> viewAdapter.onItemSwipeLeft(position) },
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
}