package org.alexcawl.todoapp.presentation.util

import android.app.Application

class TodoApplication : Application() {
    companion object {
        const val UUID: String = "UUID"
        const val isEnabled: String = "isEnabled"
    }
}