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
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.data.TodoItem
import org.alexcawl.todoapp.databinding.FragmentItemAddBinding
import org.alexcawl.todoapp.extensions.add
import org.alexcawl.todoapp.model.ItemViewModel
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
        * Priority RadioGroup
        * */
        binding.taskPriorityRadiobox.setOnCheckedChangeListener { _, checkedId ->
            taskAddState.priority = when (checkedId) {
                R.id.radiobutton_item_priority_low -> TodoItem.Companion.Priority.LOW
                R.id.radiobutton_item_priority_high -> TodoItem.Companion.Priority.HIGH
                else -> TodoItem.Companion.Priority.NORMAL
            }
        }

        /*
        * Content TextView
        * */
        binding.edittextItemContent.addTextChangedListener {
            taskAddState.text = it.toString()
        }

        /*
        * Deadline Switch
        * */
        binding.switchItemDeadline.setOnCheckedChangeListener { _, isChecked ->
            taskAddState.deadline = when(isChecked) {
                false -> null
                true -> deadlineTimeState
            }
            binding.calendarItemDeadline.visibility = when(isChecked) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

        /*
        * Deadline CalendarView
        * */
        binding.calendarItemDeadline.setOnDateChangeListener { _, year, month, dayOfMonth ->
            deadlineTimeState = LocalDateTime.of(year, month, dayOfMonth, 0, 0)
            taskAddState.deadline = deadlineTimeState
        }

        /*
        * Add Button
        * */
        binding.buttonItemAdd.setOnClickListener {
            model.items.add(taskAddState)
            navigationController.navigateUp()
        }
    }
}

