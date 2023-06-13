package org.alexcawl.todoapp.fragment

import android.os.Bundle
import android.util.Log
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
import org.alexcawl.todoapp.extensions.add
import org.alexcawl.todoapp.extensions.set
import org.alexcawl.todoapp.model.ItemViewModel
import java.time.LocalDateTime

class ItemEditFragment : Fragment() {
    private val model: ItemViewModel by lazy {
        ViewModelProvider(this.requireActivity())[ItemViewModel::class.java]
    }

    private lateinit var itemParamState: Pair<Int, TodoItem?>
    private lateinit var itemDataState: TodoItem
    private lateinit var binding: FragmentItemEditBinding
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationController = findNavController()
        retrieveStartData(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLayoutContent()
        setLayoutBinding()
    }

    private fun retrieveStartData(args: Bundle?) {
        val identifier: String? = args?.getString(model.ID_NAME)
        itemParamState = when (identifier) {
            null -> Pair(-1, null)
            else -> {
                val position = model.todoItems.value?.
                    indexOfFirst { it.identifier == identifier } ?: -1
                when (position) {
                    -1 -> Pair(position, null)
                    else -> Pair(position, model.todoItems.value?.get(position))
                }
            }
        }
        itemDataState = when(val item = itemParamState.second) {
            null -> TodoItem.createEmpty(model.getRandomID(), LocalDateTime.now())
            else -> TodoItem.of(item)
        }
    }

    private fun setLayoutContent() {
        binding.radiogroupItem.check(
            when (itemDataState.priority) {
                TodoItem.Companion.Priority.LOW -> R.id.radiobutton_item_priority_low
                TodoItem.Companion.Priority.HIGH -> R.id.radiobutton_item_priority_high
                else -> R.id.radiobutton_item_priority_normal
            }
        )

        binding.edittextItemContent.setText(itemDataState.text)

        binding.switchItemDeadline.isChecked = when (itemDataState.deadline) {
            null -> false
            else -> true
        }

        binding.calendarItemDeadline.visibility = when (itemDataState.deadline) {
            null -> View.GONE
            else -> View.VISIBLE
        }
    }

    private fun setLayoutBinding() {
        binding.radiogroupItem.setOnCheckedChangeListener { _, checkedId ->
            itemDataState.priority = when (checkedId) {
                R.id.radiobutton_item_priority_low -> TodoItem.Companion.Priority.LOW
                R.id.radiobutton_item_priority_high -> TodoItem.Companion.Priority.HIGH
                else -> TodoItem.Companion.Priority.NORMAL
            }
        }

        binding.edittextItemContent.addTextChangedListener {
            itemDataState.text = it.toString()
        }

        binding.switchItemDeadline.setOnCheckedChangeListener { _, isChecked ->
            binding.calendarItemDeadline.visibility = when (isChecked) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            // TODO сохранение дедлайна и синхронизация с календарем
            itemDataState.deadline = when(isChecked) {
                true -> itemParamState.second?.deadline
                false -> null
            }
        }

        binding.calendarItemDeadline.setOnDateChangeListener { _, year, month, dayOfMonth ->
            itemDataState.deadline = LocalDateTime.of(year, month, dayOfMonth, 0, 0)
        }

        binding.buttonItemAdd.setOnClickListener {
            when (itemParamState.first == -1) {
                false -> model.todoItems[itemParamState.first] = itemDataState
                true -> model.todoItems.add(itemDataState)
            }
            navigationController.navigateUp()
        }
    }
}