package org.alexcawl.todoapp.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.presentation.activity.MainActivity
import org.alexcawl.todoapp.presentation.compose.ToDoApplicationTheme
import org.alexcawl.todoapp.presentation.model.MainViewModel
import org.alexcawl.todoapp.presentation.model.TaskViewModel
import org.alexcawl.todoapp.presentation.model.ViewModelFactory
import org.alexcawl.todoapp.presentation.util.*
import java.util.*
import javax.inject.Inject

/**
 * Single task info without editing screen
 * @see MainViewModel
 * */
class TaskShowFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelFactory
    private val model: TaskViewModel by lazy {
        ViewModelProvider(this, modelFactory)[TaskViewModel::class.java]
    }

    private val text: StateFlow<String> by lazy { model.text }
    private val priority: StateFlow<Priority> by lazy { model.priority }
    private val deadline: StateFlow<Long?> by lazy { model.deadline }
    private val createdAt: StateFlow<Long> by lazy { model.createdAt }
    private val changedAt: StateFlow<Long> by lazy { model.changedAt }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
        return ComposeView(requireContext()).apply {
            setContent {
                ToDoApplicationTheme {
                    Screen(
                        onCloseAction = { findNavController().navigateUp() },
                        textFlow = text,
                        priorityFlow = priority,
                        deadlineFlow = deadline,
                        createdAtFlow = createdAt,
                        changedAtFlow = changedAt
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        try {
            val id: UUID = getID(arguments)
            lifecycleScope.launch {
                model.loadTask(id).collect { uiState ->
                    when (uiState) {
                        is UiState.Error -> {
                            Log.d("ERROR", uiState.cause)
                            navController.navigateUp()
                        }
                        else -> {}
                    }
                }
            }
        } catch (exception: IllegalArgumentException) {
            view.snackbar("Task ID does not exists!")
            navController.navigateUp()
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getID(arguments: Bundle?): UUID {
        val uuid = arguments?.getString("UUID") ?: throw IllegalArgumentException("Null UUID!")
        return UUID.fromString(uuid) ?: throw IllegalArgumentException("Non-valid UUID: $uuid")
    }

    @Preview(name = "Light Theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
    @Preview(name = "Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun ScreenPreview() {
        ToDoApplicationTheme {
            Screen(
                onCloseAction = {  },
                textFlow = MutableStateFlow("Task text"),
                priorityFlow = MutableStateFlow(Priority.BASIC),
                deadlineFlow = MutableStateFlow(System.currentTimeMillis()),
                createdAtFlow = MutableStateFlow(System.currentTimeMillis()),
                changedAtFlow = MutableStateFlow(System.currentTimeMillis())
            )
        }
    }

    @Composable
    private fun Screen(
        onCloseAction: () -> Unit,
        textFlow: StateFlow<String>,
        priorityFlow: StateFlow<Priority>,
        deadlineFlow: StateFlow<Long?>,
        createdAtFlow: StateFlow<Long>,
        changedAtFlow: StateFlow<Long>
    ) {
        ToDoApplicationTheme {
            Scaffold(
                backgroundColor = MaterialTheme.colors.background,
                topBar = { Toolbar(onCloseAction) }
            ) { padding ->
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TaskText(textFlow)
                    TaskPriority(priorityFlow)
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(color = MaterialTheme.colors.onBackground)
                    )
                    TaskDeadline(deadlineFlow)
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(color = MaterialTheme.colors.onBackground)
                    )
                    TaskTime(createdAtFlow, changedAtFlow)
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(color = MaterialTheme.colors.onBackground)
                    )
                    DeleteButton()
                }
            }
        }
    }

    @Composable
    private fun Toolbar(
        onCloseAction: () -> Unit
    ) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.background,
            elevation = 8.dp
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
        }
    }

    @Composable
    private fun TaskText(
        textFlow: StateFlow<String>,
    ) {
        val text by textFlow.collectAsState()

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.large
                )
                .padding(16.dp),
            text = text,
            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onPrimary)
        )
    }

    @Composable
    private fun TaskPriority(
        priorityFlow: StateFlow<Priority>,
    ) {
        val priority by priorityFlow.collectAsState()

        Column(
            modifier = Modifier.fillMaxWidth(),
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
    ) {
        val deadline by deadlineFlow.collectAsState()

        Column(
            modifier = Modifier.fillMaxWidth(),
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
    }

    @Composable
    private fun TaskTime(
        createdAtFlow: StateFlow<Long>,
        changedAtFlow: StateFlow<Long>
    ) {
        val createdAt by createdAtFlow.collectAsState()
        val changedAt by changedAtFlow.collectAsState()

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(id = R.string.created),
                    style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onPrimary),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = createdAt.toDateFormat(),
                    style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(id = R.string.modified),
                    style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onPrimary),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = changedAt.toDateFormat(),
                    style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    private fun DeleteButton() {
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.error,
                disabledBackgroundColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colors.onBackground
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.icon_delete),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = R.string.delete).uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.button
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}