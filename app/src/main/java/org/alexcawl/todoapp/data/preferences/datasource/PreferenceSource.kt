package org.alexcawl.todoapp.data.preferences.datasource

import android.content.Context
import org.alexcawl.todoapp.data.preferences.util.PreferenceConstants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceSource @Inject constructor(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(PreferenceConstants.NAME, 0)

    fun getRevision(): Int = sharedPreferences.getInt(PreferenceConstants.REVISION, 0)

    fun setRevision(newRevision: Int) {
        sharedPreferences.edit().putInt(PreferenceConstants.REVISION, newRevision).apply()
    }
}