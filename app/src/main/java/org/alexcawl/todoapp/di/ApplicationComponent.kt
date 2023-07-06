package org.alexcawl.todoapp.di

import dagger.Component

@ApplicationScope
@Component
interface ApplicationComponent {
    fun mainActivityComponent(): MainActivityComponent.Factory
}