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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentTaskShowBinding
import org.alexcawl.todoapp.domain.util.ValidationException
import org.alexcawl.todoapp.presentation.adapter.OnItemSwipeCallback
import org.alexcawl.todoapp.presentation.adapter.TaskItemAdapter
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.util.snackbar

class TaskShowFragment : Fragment() {
    private val viewModel: TaskViewModel by lazy {
        ViewModelProvider(this.requireActivity())[TaskViewModel::class.java]
    }

    private var isVisibilityAllActive: MutableStateFlow<Boolean> = MutableStateFlow(true)
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
        setupRecyclerView(binding.recyclerView, navigationController)
        setupVisibilityButton(binding.visibilityCollapsedButton)
        setupUpdateButton(binding.orderCollapsedButton)
    }

    private fun setupVisibilityButton(view: AppCompatImageButton) {
        view.setOnClickListener {
            isVisibilityAllActive.value = isVisibilityAllActive.value.not()
            view.setImageDrawable(
                when(isVisibilityAllActive.value) {
                    true -> ContextCompat.getDrawable(view.context, R.drawable.icon_visibility_on)
                    false -> ContextCompat.getDrawable(view.context, R.drawable.icon_visibility_off)
                }
            )
        }
    }

    private fun setupUpdateButton(view: AppCompatImageButton) {
        view.setOnClickListener {
            view.snackbar("TODO UPDATE") // TODO
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
                        "UUID" to it.id.toString(),
                        "isEnabled" to true
                    )
                )
            },
            onInfoClicked = {
                navController.navigate(
                    R.id.taskEditAction,
                    bundleOf(
                        "UUID" to it.id.toString(),
                        "isEnabled" to false
                    )
                )
            },
            onTaskSwipeLeft = {
                lifecycle.coroutineScope.launch(Dispatchers.IO) {
                    viewModel.removeTask(it)
                }
            },
            onTaskSwipeRight = {
                lifecycle.coroutineScope.launch(Dispatchers.IO) {
                    try {
                        viewModel.setTask(it)
                    } catch (exception: ValidationException) {
                        view.snackbar("Blank text is not allowed!")
                    }
                }
            }
        )

        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            isVisibilityAllActive.collectLatest { state ->
                when(state) {
                    true -> {
                        viewModel.getAllTasks().collectLatest {
                            viewAdapter.submitList(it)
                        }
                    }
                    false -> {
                        viewModel.getUncompletedTasks().collectLatest {
                            viewAdapter.submitList(it)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}