package org.alexcawl.todoapp.fragment

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
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.data.TodoItem
import org.alexcawl.todoapp.databinding.FragmentItemEditBinding
import org.alexcawl.todoapp.extensions.removeAt
import org.alexcawl.todoapp.extensions.set
import org.alexcawl.todoapp.model.ItemViewModel
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
        val identifier: String = args?.getString(ItemViewModel.ID_NAME) ?: throw IllegalStateException()
        val position: Int = model.items.value?.indexOfFirst { it.identifier == identifier } ?: throw IllegalStateException()
        val task: TodoItem = model.items.value?.get(position) ?: throw IllegalStateException()
        return Pair(position, TodoItem.of(task))
    }

    private fun setLayoutContent() {
        val task = taskEditState.second
        deadlineTimeState = task.deadline
        /*
        * Status Checkbox
        * */
        binding.taskStatusCheckbox.isChecked = task.isDone
        when (binding.taskStatusCheckbox.isChecked) {
            true -> binding.taskStatusCheckbox.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            false -> binding.taskStatusCheckbox.apply {
                paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        /*
        * Priority RadioGroup
        * */
        binding.taskPriorityRadiobox.check(
            when (task.priority) {
                TodoItem.Companion.Priority.LOW -> R.id.radiobutton_item_priority_low
                TodoItem.Companion.Priority.HIGH -> R.id.radiobutton_item_priority_high
                else -> R.id.radiobutton_item_priority_normal
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
        binding.taskDeadlineSwitch.isChecked = task.deadline != null
        binding.taskDeadlineTextview.text = (task.deadline ?: "").toString()

        /*
        * Deadline CalendarView
        * */
        binding.calendarView.visibility = when(binding.taskDeadlineSwitch.isChecked) {
            true -> View.VISIBLE
            false -> View.GONE
        }
        binding.calendarView.date = when(deadlineTimeState) {
            null -> ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli()
            else -> ZonedDateTime.of(deadlineTimeState, ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }

    private fun setLayoutCallback() {
        val task = taskEditState.second

        /*
        * Status Checkbox
        * */
        binding.taskStatusCheckbox.setOnCheckedChangeListener { view, isChecked ->
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
        * Priority RadioGroup
        * */
        binding.taskPriorityRadiobox.setOnCheckedChangeListener { _, checkedId ->
            task.priority = when (checkedId) {
                R.id.radiobutton_item_priority_low -> TodoItem.Companion.Priority.LOW
                R.id.radiobutton_item_priority_high -> TodoItem.Companion.Priority.HIGH
                else -> TodoItem.Companion.Priority.NORMAL
            }
        }

        /*
        * Content TextView
        * */
        binding.taskContentTextview.addTextChangedListener {
            task.text = it.toString()
        }

        /*
        * Deadline Switch
        * */
        binding.taskDeadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            task.deadline = when(isChecked) {
                false -> null
                true -> deadlineTimeState
            }
            binding.taskDeadlineTextview.text = (task.deadline ?: "").toString()
            binding.calendarView.visibility = when(isChecked) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

        /*
        * Deadline CalendarView
        * */
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            deadlineTimeState = LocalDateTime.of(year, month, dayOfMonth, 0, 0)
            task.deadline = deadlineTimeState
            binding.taskDeadlineTextview.text = (task.deadline ?: "").toString()
        }

        /*
        * Delete Button
        * */
        binding.taskDeleteButton.setOnClickListener {
            model.items.removeAt(taskEditState.first)
            navigationController.navigateUp()
        }

        /*
        * Save Button
        * */
        binding.taskSaveButton.setOnClickListener {
            task.modifyingTime = LocalDateTime.now()
            model.items[taskEditState.first] = task
            navigationController.navigateUp()
        }
    }
}