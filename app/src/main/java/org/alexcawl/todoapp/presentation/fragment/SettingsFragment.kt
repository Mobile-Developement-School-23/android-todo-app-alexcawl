package org.alexcawl.todoapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.alexcawl.todoapp.databinding.FragmentSettingsBinding
import org.alexcawl.todoapp.presentation.activity.MainActivity
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.model.TaskViewModelFactory
import javax.inject.Inject

class SettingsFragment : Fragment() {
    @Inject
    lateinit var modelFactory: TaskViewModelFactory
    private val model: TaskViewModel by lazy {
        ViewModelProvider(this, modelFactory)[TaskViewModel::class.java]
    }
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    private var isServerEnabled: Boolean = false
    private var username: String = ""
    private var token: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        val controller = model.getSettingsController()
        isServerEnabled = controller.getServerEnabled()
        username = controller.getUsername()
        token = controller.getToken()
        with(binding) {
            setupCloseButton(closeButton, navController)
            setupSaveButton(saveButton, navController)
            setupServerSwitch(serverSyncSwitch)
            setupUsernameField(loginText)
            setupTokenField(tokenText)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCloseButton(button: AppCompatImageButton, navController: NavController) {
        button.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun setupSaveButton(button: AppCompatButton, navController: NavController) {
        button.setOnClickListener {
            val controller = model.getSettingsController()
            controller.setServerEnabled(isServerEnabled)
            controller.setUsername(username)
            controller.setToken(token)
            navController.navigateUp()
        }
    }

    private fun setupServerSwitch(switch: SwitchCompat) {
        switch.isChecked = isServerEnabled
        switch.setOnCheckedChangeListener { _, isChecked ->
            isServerEnabled = isChecked
        }
    }

    private fun setupUsernameField(text: AppCompatEditText) {
        text.setText(username)
        text.addTextChangedListener {
            username = (it ?: "").toString()
        }
    }

    private fun setupTokenField(text: AppCompatEditText) {
        text.setText(token)
        text.addTextChangedListener {
            token = (it ?: "").toString()
        }
    }
}