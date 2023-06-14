package org.alexcawl.todoapp.android.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.alexcawl.todoapp.android.application.TodoApplication
import org.alexcawl.todoapp.android.model.ItemViewModel
import org.alexcawl.todoapp.android.spinner_view.SpinnerListener
import org.alexcawl.todoapp.data.model.TodoItem
import org.alexcawl.todoapp.databinding.FragmentItemEditBinding
import org.alexcawl.todoapp.extensions.removeAt
import org.alexcawl.todoapp.extensions.set
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class ItemEditFragment : Fragment() {
    private val model: ItemViewModel by lazy {
        ViewModelProvider(this.requireActivity())[ItemViewModel::class.java]
    }

    private lateinit var taskEditState: Pair<Int, TodoItem>
    private var deadlineTimeState: LocalDateTime? = null
    private lateinit var binding: FragmentItemEditBinding
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigationController = findNavController()
        try {
            taskEditState = initiateTaskEditState(arguments)
        } catch (exception: IllegalStateException) {
            navigationController.navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLayoutContent()
        setLayoutCallback()
    }

    @Throws(IllegalStateException::class)
    private fun initiateTaskEditState(args: Bundle?): Pair<Int, TodoItem> {
        val identifier: String = args?.getString(TodoApplication.IDENTIFIER)
            ?: throw IllegalStateException()
        val position: Int = model.todoItems.value?.indexOfFirst { it.identifier == identifier }
            ?: throw IllegalStateException()
        val task: TodoItem = model.todoItems.value?.get(position)
            ?: throw IllegalStateException()
        return Pair(position, TodoItem.of(task))
    }

    private fun setLayoutContent() {
        val task = taskEditState.second
        deadlineTimeState = task.deadline
        /*
        * Status Checkbox
        * */
        binding.status.taskStatusCheckbox.isChecked = task.isDone
        when (binding.status.taskStatusCheckbox.isChecked) {
            true -> binding.status.taskStatusCheckbox.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            false -> binding.status.taskStatusCheckbox.apply {
                paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        /*
        * Priority Spinner TODO
        * */
        binding.priority.taskPrioritySpinner.setSelection(
            when (task.priority) {
                TodoItem.Companion.Priority.NORMAL -> 0
                TodoItem.Companion.Priority.LOW -> 1
                TodoItem.Companion.Priority.HIGH -> 2
            }
        )

        /*
        * Creation Time TextView
        * */
        binding.taskCreationTimeTextview.text = task.creationTime.toString()

        /*
        * Modifying Time TextView
        * */
        binding.taskModifyingTimeTextview.text = (task.modifyingTime ?: "").toString()

        /*
        * Content TextView
        * */
        binding.taskContentTextview.setText(task.text)

        /*
        * Deadline Switch
        * */
        binding.deadline.taskDeadlineSwitch.isChecked = task.deadline != null
        binding.deadline.taskDeadlineTextview.text = (task.deadline ?: "").toString()

        /*
        * Deadline CalendarView
        * */
        binding.deadline.taskDeadlineCalendarview.visibility =
            when (binding.deadline.taskDeadlineSwitch.isChecked) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        binding.deadline.taskDeadlineCalendarview.date = when (deadlineTimeState) {
            null -> ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
            else -> ZonedDateTime.of(deadlineTimeState, ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
        }
    }

    private fun setLayoutCallback() {
        val task = taskEditState.second

        /*
        * Status Checkbox
        * */
        binding.status.taskStatusCheckbox.setOnCheckedChangeListener { view, isChecked ->
            task.isDone = isChecked
            when (view.isChecked) {
                true -> view.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                false -> view.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }

        /*
        * Priority Spinner TODO
        * */
        binding.priority.taskPrioritySpinner.onItemSelectedListener =
            SpinnerListener(onItemSelected = { position: Int ->
                task.priority = when (position) {
                    0 -> TodoItem.Companion.Priority.NORMAL
                    1 -> TodoItem.Companion.Priority.LOW
                    else -> TodoItem.Companion.Priority.HIGH
                }
            })

        /*
        * Content TextView
        * */
        binding.taskContentTextview.addTextChangedListener {
            task.text = it.toString()
        }

        /*
        * Deadline Switch
        * */
        binding.deadline.taskDeadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            task.deadline = when (isChecked) {
                false -> null
                true -> deadlineTimeState
            }
            binding.deadline.taskDeadlineTextview.text = (task.deadline ?: "").toString()
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
            task.deadline = deadlineTimeState
            binding.deadline.taskDeadlineTextview.text = (task.deadline ?: "").toString()
        }

        /*
        * Delete Button
        * */
        binding.taskDeleteButton.setOnClickListener {
            model.todoItems.removeAt(taskEditState.first)
            navigationController.navigateUp()
        }

        /*
        * Save Button
        * */
        binding.taskSaveButton.setOnClickListener {
            task.modifyingTime = LocalDateTime.now()
            model.todoItems[taskEditState.first] = task
            navigationController.navigateUp()
        }
    }
}