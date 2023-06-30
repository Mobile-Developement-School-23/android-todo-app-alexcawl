package org.alexcawl.todoapp.presentation.util

import android.util.Log

class PreferencesCommitter {
    private var commit: Int = 0

    fun getRevision(): Int = commit

    fun setRevision(revision: Int) {
        Log.d("REVISION-SET", revision.toString())
        commit = revision
    }
}