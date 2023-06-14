package org.alexcawl.todoapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.alexcawl.todoapp.data.TodoItem
import org.alexcawl.todoapp.databinding.FragmentItemAddBinding
import org.alexcawl.todoapp.extensions.add
import org.alexcawl.todoapp.model.ItemViewModel
import org.alexcawl.todoapp.spinner_view.SpinnerListener
import java.time.LocalDateTime

class ItemAddFragment : Fragment() {
    private val model: ItemViewModel by lazy {
        ViewModelProvider(this.requireActivity())[ItemViewModel::class.java]
    }

    private lateinit var taskAddState: TodoItem
    private var deadlineTimeState: LocalDateTime? = null
    private lateinit var binding: FragmentItemAddBinding
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationController = findNavController()
        taskAddState = initiateTaskAddState()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLayoutCallback()
    }

    private fun initiateTaskAddState(): TodoItem {
        return TodoItem.createEmpty(
            model.getRandomID(),
            LocalDateTime.now()
        )
    }

    private fun setLayoutCallback() {
        /*
        * Priority Spinner
        * */
        binding.priority.taskPrioritySpinner.onItemSelectedListener = SpinnerListener(
            onItemSelected = { position: Int ->
                taskAddState.priority = when (position) {
                    0 -> TodoItem.Companion.Priority.NORMAL
                    1 -> TodoItem.Companion.Priority.LOW
                    else -> TodoItem.Companion.Priority.HIGH
                }
            }
        )

        /*
        * Content TextView
        * */
        binding.taskContentTextview.addTextChangedListener {
            taskAddState.text = it.toString()
        }

        /*
        * Deadline Switch
        * */
        binding.deadline.taskDeadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            taskAddState.deadline = when (isChecked) {
                false -> null
                true -> deadlineTimeState
            }
            binding.deadline.taskDeadlineTextview.text = (taskAddState.deadline ?: "").toString()
            binding.deadline.taskDeadlineCalendarview.visibility = when (isChecked) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

        /*
        * Deadline CalendarView
        * */
        binding.deadline.taskDeadlineCalendarview.setOnDateChangeListener { _, year, month, dayOfMonth ->
            deadlineTimeState = LocalDateTime.of(year, month, dayOfMonth, 0, 0)
            taskAddState.deadline = deadlineTimeState
            binding.deadline.taskDeadlineTextview.text = (taskAddState.deadline ?: "").toString()
        }
        binding.deadline.taskDeadlineCalendarview.visibility = View.GONE

        /*
        * Add Button
        * */
        binding.buttonAdd.setOnClickListener {
            model.items.add(taskAddState)
            navigationController.navigateUp()
        }
    }
}

