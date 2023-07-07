package org.alexcawl.todoapp.data.preferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Shared preferneces data source
 * @param context Application context
 * @see SharedPreferences
 * */
class PreferenceSource @Inject constructor(
    context: Context
) {
    private companion object {
        const val NAME: String = "ToDoPref"
        const val REVISION: String = "REVISION"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun getRevision(): Int = sharedPreferences.getInt(REVISION, 0)

    fun setRevision(newRevision: Int) {
        sharedPreferences.edit().putInt(REVISION, newRevision).apply()
    }
}