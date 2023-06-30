package org.alexcawl.todoapp.presentation.util

import android.content.Context
import android.util.Log

class PreferencesCommitter(
    val context: Context
) {
    private val preferences = context.getSharedPreferences("ToDoPref", 0)

    fun getRevision(): Int = preferences.getInt("REVISION", 0)

    fun setRevision(revision: Int) {
        Log.d("REVISION-SET", revision.toString())
        preferences.edit().putInt("REVISION", revision).apply()
        Log.d("REVISION-CHECK", getRevision().toString())
    }

    fun updateRevision(): Unit {
        val current = getRevision()
        preferences.edit().putInt("REVISION", current + 1).apply()
        Log.d("REVISION-CHECK-UPDATE", getRevision().toString())
    }
}