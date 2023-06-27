package org.alexcawl.todoapp.presentation.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
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
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentTaskShowBinding
import org.alexcawl.todoapp.presentation.adapter.OnItemDragCallback
import org.alexcawl.todoapp.presentation.adapter.OnItemSwipeCallback
import org.alexcawl.todoapp.presentation.adapter.TaskItemAdapter
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.util.TodoApplication
import org.alexcawl.todoapp.presentation.util.snackbar

class TaskShowFragment : Fragment() {
    private val viewModel: TaskViewModel by lazy {
        ViewModelProvider(this.requireActivity())[TaskViewModel::class.java]
    }

    private var _binding: FragmentTaskShowBinding? = null
    private val binding: FragmentTaskShowBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navigationController = findNavController()
        setupActionButton(binding.fab, navigationController)
        setupVisibilityButton(binding.visibilityCollapsedButton)
        setupOrderingButton(binding.orderCollapsedButton)
        setupRecyclerView(binding.recyclerView, navigationController)
    }

    private fun setupVisibilityButton(view: AppCompatImageButton) {
        view.setOnClickListener {
            view.snackbar("TODO VISIBILITY") // TODO
        }
    }

    private fun setupOrderingButton(view: AppCompatImageButton) {
        view.setOnClickListener {
            view.snackbar("TODO ORDERING") // TODO
        }
    }

    private fun setupActionButton(view: FloatingActionButton, navController: NavController) {
        view.setOnClickListener {
            navController.navigate(R.id.taskAddAction)
        }
    }

    private fun setupRecyclerView(view: RecyclerView, navController: NavController) {
        // Layout manager
        val viewManager = LinearLayoutManager(context)

        // RecyclerViewAdapter
        val viewAdapter = TaskItemAdapter(
            onEditClicked = {
                navController.navigate(
                    R.id.taskEditAction,
                    bundleOf(
                        TodoApplication.UUID to it.id.toString(),
                        TodoApplication.isEnabled to true
                    )
                )
            },
            onInfoClicked = {
                navController.navigate(
                    R.id.taskEditAction,
                    bundleOf(
                        TodoApplication.UUID to it.id.toString(),
                        TodoApplication.isEnabled to false
                    )
                )
            },
            onTaskSwipeLeft = {
                lifecycle.coroutineScope.launch {
                    viewModel.removeTask(it)
                }
            },
            onTaskSwipeRight = {
                lifecycle.coroutineScope.launch {
                    viewModel.setTask(it)
                }
            },
            onTaskDrag = {
                lifecycle.coroutineScope.launch {
                    viewModel.setTasks(it)
                }
            }
        )

        // Flow resource binding
        lifecycle.coroutineScope.launch {
            viewModel.getAllTasks().collect {
                viewAdapter.submitList(it)
            }
        }

        // Drag support helper
        val dragHelper = ItemTouchHelper(
            OnItemDragCallback { from, to -> viewAdapter.onItemDrag(from, to) }
        )

        // Swipe support helper
        val swipeHelper = ItemTouchHelper(
            OnItemSwipeCallback(
                { position ->
                    viewAdapter.onItemSwipeLeft(position)
                },
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
            dragHelper.attachToRecyclerView(view)
            swipeHelper.attachToRecyclerView(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}