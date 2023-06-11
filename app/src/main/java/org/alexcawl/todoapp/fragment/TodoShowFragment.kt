package org.alexcawl.todoapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentTodoShowBinding

class TodoShowFragment : Fragment() {
    private lateinit var binding: FragmentTodoShowBinding
    private lateinit var navigationController: NavController

    private lateinit var floatingActionButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoShowBinding.inflate(inflater, container, false)
        floatingActionButton = binding.fab

        initNavigationRoutes()

        return binding.root
    }

    private fun initNavigationRoutes() {
        floatingActionButton.setOnClickListener {
            navigationController.navigate(R.id.action_todoShow_to_todoAdd)
        }
    }
}