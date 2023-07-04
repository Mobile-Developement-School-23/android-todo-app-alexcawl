package org.alexcawl.todoapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
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
    fun provideSharedPreferences(context: Context) = PreferencesCommitter(context)
}