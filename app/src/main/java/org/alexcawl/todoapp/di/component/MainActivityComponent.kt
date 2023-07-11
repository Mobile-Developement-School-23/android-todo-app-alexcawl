package org.alexcawl.todoapp.di.component

import dagger.Subcomponent
import org.alexcawl.todoapp.di.module.UseCaseModule
import org.alexcawl.todoapp.di.scope.MainActivityScope
import org.alexcawl.todoapp.presentation.fragment.TaskAddFragment
import org.alexcawl.todoapp.presentation.fragment.TaskEditFragment
import org.alexcawl.todoapp.presentation.fragment.TaskListFragment
import org.alexcawl.todoapp.presentation.fragment.TaskShowFragment

@MainActivityScope
@Subcomponent(modules = [UseCaseModule::class])
interface MainActivityComponent {

    fun inject(fragment: TaskListFragment)

    fun inject(fragment: TaskAddFragment)

    fun inject(fragment: TaskEditFragment)

    fun inject(fragment: TaskShowFragment)
}