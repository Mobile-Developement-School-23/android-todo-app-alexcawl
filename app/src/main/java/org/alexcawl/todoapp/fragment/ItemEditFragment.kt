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
import org.alexcawl.todoapp.extensions.get
import org.alexcawl.todoapp.extensions.set
import org.alexcawl.todoapp.model.ItemViewModel
import java.time.LocalDateTime

class ItemEditFragment : Fragment() {
    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this.requireActivity())[ItemViewModel::class.java]
    }

    private var previousPosition: Int = -1
    private lateinit var savedItemState: TodoItem
    private lateinit var binding: FragmentItemEditBinding
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigationController = findNavController()

        val identifier = savedInstanceState?.getString("identifier") ?: itemViewModel.getRandomID()
        Log.println(Log.INFO, "ABOBA", itemViewModel.todoItems.value.toString())
        Log.println(Log.INFO, "ABOBA", savedInstanceState?.getString("identifier").toString())
        Log.println(Log.INFO, "ABOBA", savedInstanceState.toString())
        val item = itemViewModel.todoItems.value?.find { it.identifier == identifier }
        savedItemState = when (item) {
            null -> TodoItem(
                identifier,
                "",
                TodoItem.Companion.Priority.NORMAL,
                false,
                LocalDateTime.now()
            )
            else -> {
                item.modifyingTime = LocalDateTime.now()
                previousPosition = itemViewModel.todoItems.value?.indexOf(item) ?: -1
                item
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        restoreSavedItemState()
        setLayoutBinding()
    }

    private fun restoreSavedItemState() {
        binding.radiogroupItem.check(
            when (savedItemState.priority) {
                TodoItem.Companion.Priority.LOW -> R.id.radiobutton_item_priority_low
                TodoItem.Companion.Priority.HIGH -> R.id.radiobutton_item_priority_high
                else -> R.id.radiobutton_item_priority_normal
            }
        )

        binding.edittextItemContent.setText(savedItemState.text)

        binding.switchItemDeadline.isChecked = when (savedItemState.deadline) {
            null -> false
            else -> true
        }

        binding.calendarItemDeadline.visibility = when (savedItemState.deadline) {
            null -> View.GONE
            else -> View.VISIBLE
        }
    }

    private fun setLayoutBinding() {
        binding.radiogroupItem.setOnCheckedChangeListener { _, checkedId ->
            savedItemState.priority = when (checkedId) {
                R.id.radiobutton_item_priority_low -> TodoItem.Companion.Priority.LOW
                R.id.radiobutton_item_priority_high -> TodoItem.Companion.Priority.HIGH
                else -> TodoItem.Companion.Priority.NORMAL
            }
        }

        binding.edittextItemContent.addTextChangedListener {
            savedItemState.text = it.toString()
        }

        binding.switchItemDeadline.setOnCheckedChangeListener { _, isChecked ->
            binding.calendarItemDeadline.visibility = when (isChecked) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

        binding.calendarItemDeadline.setOnDateChangeListener { _, year, month, dayOfMonth ->
            savedItemState.deadline = LocalDateTime.of(year, month, dayOfMonth, 0, 0)
        }

        binding.buttonItemAdd.setOnClickListener {
            when (previousPosition == -1) {
                false -> itemViewModel.todoItems[previousPosition] = savedItemState
                true -> itemViewModel.todoItems.add(savedItemState)
            }
            navigationController.navigateUp()
        }
    }
}