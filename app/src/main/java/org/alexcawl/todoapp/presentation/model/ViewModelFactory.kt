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
class ViewModelFactory @Inject constructor(
    private val updateCase: IUpdateTaskUseCase,
    private val getAllCase: IGetTasksUseCase,
    private val getSingleCase: IGetTaskUseCase,
    private val deleteCase: IDeleteTaskUseCase,
    private val addCase: IAddTaskUseCase,
    private val syncCase: ISyncUseCase,
    private val settingsUseCase: ISettingsOperateUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass) {
            MainViewModel::class.java -> MainViewModel(
                getAllCase = getAllCase,
                syncCase = syncCase,
                settingsUseCase = settingsUseCase,
                updateCase = updateCase,
                deleteCase = deleteCase
            ) as T
            TaskViewModel::class.java -> TaskViewModel(
                getSingleCase = getSingleCase,
                updateCase = updateCase,
                deleteCase = deleteCase
            ) as T
            NewTaskViewModel::class.java -> NewTaskViewModel(
                addCase = addCase
            ) as T
            SettingsViewModel::class.java -> SettingsViewModel(
                settingsUseCase = settingsUseCase
            ) as T
            else -> throw IllegalArgumentException("Big shit bro, wrong VM!")
        }
    }
}
