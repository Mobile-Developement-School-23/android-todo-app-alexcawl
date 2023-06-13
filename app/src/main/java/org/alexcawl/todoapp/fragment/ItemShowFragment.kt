package org.alexcawl.todoapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentItemShowBinding
import org.alexcawl.todoapp.model.ItemViewModel
import org.alexcawl.todoapp.recycle_view.ItemAdapter

class ItemShowFragment : Fragment() {
    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this.requireActivity())[ItemViewModel::class.java]
    }

    private lateinit var binding: FragmentItemShowBinding
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
        binding = FragmentItemShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fab.setOnClickListener {
            navigationController.navigate(R.id.action_taskShow_to_taskAdd)
        }

        val manager = LinearLayoutManager(context) // LayoutManager
        val adapter = ItemAdapter(itemViewModel.items.value!!) {
            navigationController.navigate(
                R.id.action_taskShow_to_taskEdit,
                bundleOf("identifier" to it.identifier)
            )
        }
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
    }
}