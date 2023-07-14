package org.alexcawl.todoapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.FragmentSettingsBinding
import org.alexcawl.todoapp.presentation.activity.MainActivity
import org.alexcawl.todoapp.presentation.model.SettingsViewModel
import org.alexcawl.todoapp.presentation.model.ViewModelFactory
import org.alexcawl.todoapp.presentation.util.ThemeState
import javax.inject.Inject

class SettingsFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelFactory
    private val model: SettingsViewModel by lazy {
        ViewModelProvider(this, modelFactory)[SettingsViewModel::class.java]
    }
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        with(binding) {
            setupCloseButton(closeButton, navController)
            setupSaveButton(saveButton, navController)
            setupServerSwitch(serverSyncSwitch)
            setupUsernameField(loginText)
            setupTokenField(tokenText)
            setupThemeButtons(buttonToggleGroup)
            setupNotificationSwitch(notificationSwitch)
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
            model.saveSettings()
            navController.navigateUp()
        }
    }

    private fun setupServerSwitch(switch: SwitchCompat) {
        lifecycle.coroutineScope.launch {
            model.networkEnabled.collect {
                switch.isChecked = it
            }
        }
        switch.setOnCheckedChangeListener { _, isChecked ->
            model.setServerEnabled(isChecked)
        }
    }

    private fun setupUsernameField(text: AppCompatEditText) {
        lifecycle.coroutineScope.launch {
            model.username.collect {
                text.setText(it)
            }
        }
        text.addTextChangedListener {
            model.setUsername((it ?: "").toString())
        }
    }

    private fun setupTokenField(text: AppCompatEditText) {
        lifecycle.coroutineScope.launch {
            model.token.collect {
                text.setText(it)
            }
        }
        text.addTextChangedListener {
            model.setToken((it ?: "").toString())
        }
    }

    private fun setupNotificationSwitch(switch: SwitchCompat) {
        lifecycle.coroutineScope.launch {
            model.notificationEnabled.collect {
                switch.isChecked = it
            }
        }
        switch.setOnCheckedChangeListener { _, isChecked ->
            model.setNotificationEnabled(isChecked)
        }
    }

    private fun setupThemeButtons(toggle: MaterialButtonToggleGroup) {
        when(model.getTheme()) {
            ThemeState.DEFAULT -> toggle.check(R.id.default_mode_button)
            ThemeState.DARK -> toggle.check(R.id.dark_mode_button)
            ThemeState.LIGHT -> toggle.check(R.id.light_mode_button)
        }
        toggle.addOnButtonCheckedListener { _, checkedButton: Int, isChecked: Boolean ->
            if (isChecked) {
                when (checkedButton) {
                    R.id.dark_mode_button -> {
                        model.setTheme(ThemeState.DARK)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    R.id.light_mode_button -> {
                        model.setTheme(ThemeState.LIGHT)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    else -> {
                        model.setTheme(ThemeState.DEFAULT)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
            }
        }
    }
}