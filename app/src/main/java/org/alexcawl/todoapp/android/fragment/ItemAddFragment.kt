package org.alexcawl.todoapp.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.alexcawl.todoapp.android.model.ItemViewModel
import org.alexcawl.todoapp.android.spinner_view.SpinnerListener
import org.alexcawl.todoapp.data.model.TodoItem
import org.alexcawl.todoapp.databinding.FragmentItemAddBinding
import org.alexcawl.todoapp.extensions.add
import java.util.*

class ItemAddFragment : Fragment() {
    private val model: ItemViewModel by lazy {
        ViewModelProvider(this.requireActivity())[ItemViewModel::class.java]
    }

    private lateinit var itemState: TodoItem
    private var timeState: Calendar? = null
    private lateinit var binding: FragmentItemAddBinding
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationController = findNavController()
        itemState = initiateTaskAddState()
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
            Calendar.getInstance()
        )
    }

    private fun setLayoutCallback() {
        /*
        * Priority Spinner
        * */
        binding.priority.taskPrioritySpinner.onItemSelectedListener = SpinnerListener(
            onItemSelected = { position: Int ->
                itemState.priority = when (position) {
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
            itemState.text = it.toString()
        }

        /*
        * Deadline Switch
        * */
        binding.deadline.taskDeadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            itemState.deadline = when (isChecked) {
                false -> null
                true -> timeState
            }
            binding.deadline.taskDeadlineTextview.text = model.representTimeUpToDays(itemState.deadline)
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
            itemState.deadline = timeState
            binding.deadline.taskDeadlineTextview.text = model.representTimeUpToDays(itemState.deadline)
        }
        binding.deadline.taskDeadlineCalendarview.visibility = View.GONE

        /*
        * Add Button
        * */
        binding.buttonAdd.setOnClickListener {
            model.todoItems.add(itemState)
            navigationController.navigateUp()
        }
    }
}

