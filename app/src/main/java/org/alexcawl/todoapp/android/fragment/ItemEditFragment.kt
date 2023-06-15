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
import java.util.*

class ItemEditFragment : Fragment() {
    private val model: ItemViewModel by lazy {
        ViewModelProvider(this.requireActivity())[ItemViewModel::class.java]
    }

    private lateinit var itemState: Pair<Int, TodoItem>
    private var timeState: Calendar? = null
    private lateinit var binding: FragmentItemEditBinding
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigationController = findNavController()
        try {
            itemState = initiateTaskEditState(arguments)
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
        val identifier: String =
            args?.getString(TodoApplication.IDENTIFIER) ?: throw IllegalStateException()
        val position: Int = model.todoItems.value?.indexOfFirst { it.identifier == identifier }
            ?: throw IllegalStateException()
        val task: TodoItem = model.todoItems.value?.get(position) ?: throw IllegalStateException()
        return Pair(position, TodoItem.of(task))
    }

    private fun setLayoutContent() {
        val task = itemState.second
        timeState = task.deadline
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
        * Priority Spinner
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
        binding.taskCreationTimeTextview.text = model.representTimeUpToMinutes(task.creationTime)

        /*
        * Modifying Time TextView
        * */
        binding.taskModifyingTimeTextview.text = model.representTimeUpToMinutes(task.modifyingTime)

        /*
        * Content TextView
        * */
        binding.taskContentTextview.setText(task.text)

        /*
        * Deadline Switch
        * */
        binding.deadline.taskDeadlineSwitch.isChecked = task.deadline != null
        binding.deadline.taskDeadlineTextview.text = model.representTimeUpToDays(task.deadline)

        /*
        * Deadline CalendarView
        * */
        binding.deadline.taskDeadlineCalendarview.visibility =
            when (binding.deadline.taskDeadlineSwitch.isChecked) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        binding.deadline.taskDeadlineCalendarview.date = task.deadline?.timeInMillis
            ?: Calendar.getInstance().timeInMillis
    }

    private fun setLayoutCallback() {
        val task = itemState.second

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
                true -> timeState
            }
            binding.deadline.taskDeadlineTextview.text = model.representTimeUpToDays(task.deadline)
            binding.deadline.taskDeadlineCalendarview.visibility = when (isChecked) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

        /*
        * Deadline CalendarView
        * */
        binding.deadline.taskDeadlineCalendarview.setOnDateChangeListener { _, year, month, dayOfMonth ->
            timeState = Calendar.Builder().setDate(year, month, dayOfMonth).build()
            task.deadline = timeState
            binding.deadline.taskDeadlineTextview.text = model.representTimeUpToDays(task.deadline)
        }

        /*
        * Delete Button
        * */
        binding.taskDeleteButton.setOnClickListener {
            model.todoItems.removeAt(itemState.first)
            navigationController.navigateUp()
        }

        /*
        * Save Button
        * */
        binding.taskSaveButton.setOnClickListener {
            task.modifyingTime = Calendar.getInstance()
            model.todoItems[itemState.first] = task
            navigationController.navigateUp()
        }
    }
}