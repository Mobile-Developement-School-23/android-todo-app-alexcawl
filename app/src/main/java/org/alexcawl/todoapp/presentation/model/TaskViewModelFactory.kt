package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.alexcawl.todoapp.di.scope.MainActivityScope
import org.alexcawl.todoapp.domain.usecases.*
import javax.inject.Inject

@MainActivityScope
class TaskViewModelFactory @Inject constructor(
    private val updateCase: IUpdateTaskUseCase,
    private val getAllCase: IGetTasksUseCase,
    private val getSingleCase: IGetTaskUseCase,
    private val removeCase: IDeleteTaskUseCase,
    private val addCase: IAddTaskUseCase,
    private val syncCase: ISyncUseCase
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
