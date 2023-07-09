package org.alexcawl.todoapp.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.alexcawl.todoapp.di.scope.MainActivityScope
import org.alexcawl.todoapp.domain.usecases.*
import javax.inject.Inject

/**
 * Custom ViewModel factory for easy dependency injection
 * @param updateCase Task update use case
 * @param deleteCase Task remove use case
 * @param addCase Task add use case
 * @param getAllCase Tasks get all use case
 * @param getSingleCase Task get single use case
 * @param syncCase Task synchronizing use case
 * @see ViewModel
 * @see ViewModelProvider
 * @see ViewModelProvider.Factory
 * */
@MainActivityScope
class TaskViewModelFactory @Inject constructor(
    private val updateCase: IUpdateTaskUseCase,
    private val getAllCase: IGetTasksUseCase,
    private val getSingleCase: IGetTaskUseCase,
    private val deleteCase: IDeleteTaskUseCase,
    private val addCase: IAddTaskUseCase,
    private val syncCase: ISyncUseCase,
    private val settingsUseCase: ISettingsOperateUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = TaskViewModel(
        updateCase = updateCase,
        getAllCase = getAllCase,
        getSingleCase = getSingleCase,
        deleteCase = deleteCase,
        addCase = addCase,
        syncCase = syncCase,
        settingsUseCase = settingsUseCase
    ) as T
}
