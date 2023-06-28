package org.alexcawl.todoapp.domain.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.service.TaskService

object TaskGetAllUseCase {
    private val service: TaskService = TaskService.getInstance()

    operator fun invoke(): StateFlow<List<TaskModel>> = service.getAllTasks().stateIn(
        CoroutineScope(Dispatchers.IO),
        SharingStarted.Lazily,
        listOf()
    )
}