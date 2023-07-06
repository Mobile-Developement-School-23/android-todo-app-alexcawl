package org.alexcawl.todoapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.alexcawl.todoapp.data.database.dao.TaskDao
import org.alexcawl.todoapp.data.database.db.ApplicationDatabase

@Module
interface DatabaseModule {
    companion object {
        @Provides
        @MainActivityScope
        fun provideDatabase(context: Context): ApplicationDatabase = Room
            .databaseBuilder(
                context,
                ApplicationDatabase::class.java,
                "task_database"
            ).build()

        @Provides
        @MainActivityScope
        fun provideTaskDao(database: ApplicationDatabase): TaskDao = database.taskDao()
    }
}