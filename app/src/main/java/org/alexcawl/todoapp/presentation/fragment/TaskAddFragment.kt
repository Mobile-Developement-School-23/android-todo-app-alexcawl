package org.alexcawl.todoapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.presentation.activity.MainActivity
import org.alexcawl.todoapp.presentation.compose.ToDoApplicationTheme
import org.alexcawl.todoapp.presentation.model.MainViewModel
import org.alexcawl.todoapp.presentation.model.NewTaskViewModel
import org.alexcawl.todoapp.presentation.model.ViewModelFactory
import org.alexcawl.todoapp.presentation.util.*
import java.util.*
import javax.inject.Inject

/**
 * Task adding screen
 * @see MainViewModel
 * */
class TaskAddFragment : Fragment(), PriorityDialogFragment.Listener {
    @Inject
    lateinit var modelFactory: ViewModelFactory
    private val model: NewTaskViewModel by lazy {
        ViewModelProvider(this, modelFactory)[NewTaskViewModel::class.java]
    }

    private val text: StateFlow<String> by lazy { model.text }
    private val priority: StateFlow<Priority> by lazy { model.priority }
    private val deadline: StateFlow<Long?> by lazy { model.deadline }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
        return ComposeView(requireContext()).apply {
            setContent {
                ToDoApplicationTheme {
                    Screen(
                        onCloseAction = { findNavController().navigateUp() },
                        onAddAction = {
                            lifecycle.coroutineScope.launch {
                                model.addTask().collect { uiState ->
                                    when (uiState) {
                                        is UiState.OK -> findNavController().navigateUp()
                                        is UiState.Error -> {
                                            this@apply.snackbar(uiState.cause)
//                                            findNavController().navigateUp()
                                        }
                                        else -> {}
                                    }
                                }
                            }
                        },
                        textFlow = text,
                        onChangeTextAction = { model.setTaskText(it) },
                        priorityFlow = priority,
                        onPriorityClickAction = {
                            PriorityDialogFragment(priority.value, this@TaskAddFragment)
                                .show(parentFragmentManager,"PRIORITY-DIALOG")
                        },
                        deadlineFlow = deadline,
                        onSwitchActivation = {
                            val dateString = createDateString(Calendar.getInstance())
                            model.setTaskDeadline(dateStringToTimestamp(dateString))
                        },
                        onSwitchDeactivation = {
                            model.setTaskDeadline(null)
                        },
                        onDeadlineClickAction = {
                            requireContext().pickDateAndTime { date ->
                                val timestamp = createDateString(date)
                                model.setTaskDeadline(dateStringToTimestamp(timestamp))
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onSubmit(priority: Priority) = model.setTaskPriority(priority)

    @Composable
    private fun Screen(
        onCloseAction: () -> Unit,
        onAddAction: () -> Unit,
        textFlow: StateFlow<String>,
        onChangeTextAction: (String) -> Unit,
        priorityFlow: StateFlow<Priority>,
        onPriorityClickAction: () -> Unit,
        deadlineFlow: StateFlow<Long?>,
        onSwitchActivation: () -> Unit,
        onSwitchDeactivation: () -> Unit,
        onDeadlineClickAction: () -> Unit
    ) {
        ToDoApplicationTheme {
            Scaffold(
                backgroundColor = MaterialTheme.colors.background,
                topBar = { Toolbar(onCloseAction, onAddAction) }
            ) { padding ->
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TaskText(textFlow, onChangeTextAction)
                    TaskPriority(priorityFlow, onPriorityClickAction)
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(color = MaterialTheme.colors.onBackground)
                    )
                    TaskDeadline(
                        deadlineFlow,
                        onSwitchActivation,
                        onSwitchDeactivation,
                        onDeadlineClickAction
                    )
                }
            }
        }
    }

    @Composable
    private fun Toolbar(
        onCloseAction: () -> Unit, onAddAction: () -> Unit
    ) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.background, elevation = 8.dp
        ) {
            IconButton(
                onClick = onCloseAction
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_clear),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onAddAction,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary
                ),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.add_task).uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.button
                )
            }
        }
    }

    @Composable
    private fun TaskText(
        textFlow: StateFlow<String>, onChangeTextAction: (String) -> Unit
    ) {
        val content by textFlow.collectAsState()
        var hasFocus by remember { mutableStateOf(false) }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.large
                )
                .onFocusChanged { hasFocus = it.hasFocus },
            value = content,
            onValueChange = onChangeTextAction,
            shape = MaterialTheme.shapes.large,
            textStyle = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.onPrimary
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = Color.Transparent
            ),
            label = {
                Text(
                    text = stringResource(id = R.string.enter_text),
                    style = MaterialTheme.typography.body1.copy(
                        color = when (hasFocus) {
                            true -> MaterialTheme.colors.primary
                            false -> MaterialTheme.colors.onSecondary
                        }
                    )
                )
            }
        )
    }

    @Composable
    private fun TaskPriority(
        priorityFlow: StateFlow<Priority>, onPriorityClickAction: () -> Unit
    ) {
        val priority by priorityFlow.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPriorityClickAction() },
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.priority),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onPrimary)
            )
            Text(
                text = when (priority) {
                    Priority.LOW -> stringResource(id = R.string.low)
                    Priority.BASIC -> stringResource(id = R.string.basic)
                    Priority.IMPORTANT -> stringResource(id = R.string.high)
                },
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.onSecondary)
            )
        }
    }

    @Composable
    private fun TaskDeadline(
        deadlineFlow: StateFlow<Long?>,
        onSwitchActivation: () -> Unit,
        onSwitchDeactivation: () -> Unit,
        onDeadlineClickAction: () -> Unit
    ) {
        val deadline by deadlineFlow.collectAsState()
        val clickable = (deadline != null)

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(clickable) { onDeadlineClickAction() },
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.deadline),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onPrimary)
                )
                Text(
                    text = when (val currentDeadline = deadline) {
                        null -> stringResource(id = R.string.not_defined)
                        else -> currentDeadline.toDateFormat()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary)
                )
            }
            Switch(
                checked = clickable,
                onCheckedChange = {
                    when (it) {
                        true -> onSwitchActivation()
                        false -> onSwitchDeactivation()
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}