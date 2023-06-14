package org.alexcawl.todoapp.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.android.application.TodoApplication
import org.alexcawl.todoapp.databinding.FragmentItemShowBinding
import org.alexcawl.todoapp.android.model.ItemViewModel
import org.alexcawl.todoapp.android.recycler_view.ItemAdapter
import org.alexcawl.todoapp.android.recycler_view.ItemTouchHelperCallback

class ItemShowFragment : Fragment() {
    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this.requireActivity())[ItemViewModel::class.java]
    }

    private lateinit var binding: FragmentItemShowBinding
    private lateinit var navigationController: NavController
    private lateinit var helper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fab.setOnClickListener {
            navigationController.navigate(R.id.action_taskShow_to_taskAdd)
        }

        val manager = LinearLayoutManager(context) // LayoutManager
        val adapter = ItemAdapter(itemViewModel.todoItems.value!!) {
            navigationController.navigate(
                R.id.action_taskShow_to_taskEdit,
                bundleOf(
                    TodoApplication.IDENTIFIER to it.identifier
                )
            )
        }
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

        val callback = ItemTouchHelperCallback(adapter)
        helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(binding.recyclerView)
    }
}