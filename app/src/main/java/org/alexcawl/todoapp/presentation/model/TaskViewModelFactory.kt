package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.alexcawl.todoapp.domain.usecases.*

class TaskViewModelFactory(
    private val updateCase: TaskUpdateUseCase,
    private val getAllCase: TaskGetAllUseCase,
    private val getUncompletedCase: TaskGetUncompletedUseCase,
    private val getSingleCase: TaskGetByIdUseCase,
    private val removeCase: TaskRemoveUseCase,
    private val addCase: TaskAddUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = TaskViewModel(
        updateCase = updateCase,
        getAllCase = getAllCase,
        getUncompletedCase = getUncompletedCase,
        getSingleCase = getSingleCase,
        removeCase = removeCase,
        addCase = addCase
    ) as T
}