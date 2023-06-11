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
import org.alexcawl.todoapp.databinding.FragmentTodoAddBinding
import org.alexcawl.todoapp.model.TodoViewModel

class TodoAddFragment : Fragment() {
    private val todoViewModel: TodoViewModel by lazy {
        ViewModelProvider(this.requireActivity())[TodoViewModel::class.java]
    }
    private lateinit var binding: FragmentTodoAddBinding
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoAddBinding.inflate(inflater, container, false)

        /*
        * Binding tracking radio group status
        * */
        binding.radiogroupItem.setOnCheckedChangeListener { _, checkedId ->
            todoViewModel.addItemType.value = when(checkedId) {
                R.id.radiobutton_item_priority_low -> TodoItem.Companion.Priority.LOW
                R.id.radiobutton_item_priority_high -> TodoItem.Companion.Priority.HIGH
                else -> TodoItem.Companion.Priority.NORMAL
            }
        }

        /*
        * Binding tracking switch status
        * */
        binding.switchItemDeadline.setOnCheckedChangeListener { _, isChecked ->
            binding.calendarItemDeadline.visibility = when(isChecked) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

        /*
        * Binding tracking edit text content
        * */
        binding.edittextItemContent.addTextChangedListener {
            todoViewModel.addItemText.value = it.toString()
        }

        /*
        * Binding clicking on button
        * */
        binding.buttonItemAdd.setOnClickListener {
            navigationController.navigateUp()
        }

        return binding.root
    }


}