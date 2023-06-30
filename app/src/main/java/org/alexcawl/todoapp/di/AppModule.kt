package org.alexcawl.todoapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.alexcawl.todoapp.domain.usecases.*
import org.alexcawl.todoapp.presentation.model.TaskViewModelFactory
import org.alexcawl.todoapp.presentation.util.PreferencesCommitter
import javax.inject.Singleton

@Module
class AppModule(
    private val context: Context
) {
    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideTaskViewModelFactory(
        editCase: TaskUpdateUseCase,
        getAllCase: TaskGetAllUseCase,
        getUncompletedCase: TaskGetUncompletedUseCase,
        getSingleCase: TaskGetByIdUseCase,
        removeCase: TaskRemoveUseCase,
        addCase: TaskAddUseCase
    ): TaskViewModelFactory = TaskViewModelFactory(
        updateCase = editCase,
        getAllCase = getAllCase,
        getUncompletedCase = getUncompletedCase,
        getSingleCase = getSingleCase,
        removeCase = removeCase,
        addCase = addCase
    )

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) = PreferencesCommitter()
}