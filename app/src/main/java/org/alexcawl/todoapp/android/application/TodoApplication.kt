package org.alexcawl.todoapp.android.application

import android.app.Application
import org.alexcawl.todoapp.service.ConverterService

class TodoApplication : Application() {
    val converterService: ConverterService = ConverterService.getInstance()

    companion object {
        const val IDENTIFIER: String = "identifier"
    }
}