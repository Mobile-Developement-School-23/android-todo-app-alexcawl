package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.alexcawl.todoapp.domain.usecases.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskViewModelFactory @Inject constructor(
    private val updateCase: UpdateTaskUseCase,
    private val getAllCase: GetTasksUseCase,
    private val getSingleCase: GetTaskUseCase,
    private val removeCase: DeleteTaskUseCase,
    private val addCase: AddTaskUseCase,
    private val syncCase: SynchronizeUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = TaskViewModel(
        updateCase = updateCase,
        getAllCase = getAllCase,
        getSingleCase = getSingleCase,
        deleteCase = removeCase,
        addCase = addCase,
        syncCase = syncCase
    ) as T
}