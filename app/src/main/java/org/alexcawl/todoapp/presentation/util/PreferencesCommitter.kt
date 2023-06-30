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

    fun updateRevision() {
        /*
        * Поскольку на бэке каждая итерация делает +1 не совсем ясно, что было раньше.
        * Если мы начинаем изменять в оффлайне, патч полностью изменит все последующие данные на сервере,
        * не синхронизировав их.
        * */
        /*val current = getRevision()
        preferences.edit().putInt("REVISION", 0).apply()
        Log.d("REVISION-CHECK-UPDATE", getRevision().toString())*/
    }
}