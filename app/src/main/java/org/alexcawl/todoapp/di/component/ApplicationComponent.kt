package org.alexcawl.todoapp.di.component

import dagger.Component
import org.alexcawl.todoapp.di.scope.ApplicationScope

@ApplicationScope
@Component
interface ApplicationComponent {
    fun mainActivityComponent(): MainActivityComponent.Factory
}