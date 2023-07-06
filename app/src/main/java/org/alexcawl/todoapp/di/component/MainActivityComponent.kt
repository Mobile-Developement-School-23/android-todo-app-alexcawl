package org.alexcawl.todoapp.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import org.alexcawl.todoapp.di.module.DatabaseModule
import org.alexcawl.todoapp.di.module.NetworkModule
import org.alexcawl.todoapp.di.module.RepositoryModule
import org.alexcawl.todoapp.di.module.UseCaseModule
import org.alexcawl.todoapp.di.scope.MainActivityScope
import org.alexcawl.todoapp.presentation.fragment.TaskAddFragment
import org.alexcawl.todoapp.presentation.fragment.TaskEditFragment
import org.alexcawl.todoapp.presentation.fragment.TaskListFragment
import org.alexcawl.todoapp.presentation.fragment.TaskShowFragment

@MainActivityScope
@Subcomponent(modules = [DatabaseModule::class, NetworkModule::class, UseCaseModule::class, RepositoryModule::class])
interface MainActivityComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MainActivityComponent
    }

    fun inject(fragment: TaskListFragment)

    fun inject(fragment: TaskAddFragment)

    fun inject(fragment: TaskEditFragment)

    fun inject(fragment: TaskShowFragment)
}