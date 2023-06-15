package org.alexcawl.todoapp.service

import java.util.*

class ConverterService private constructor() {
    companion object {
        @Volatile
        private var instance: ConverterService? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ConverterService().also { instance = it }
        }
    }

    fun getUpToDays(calendar: Calendar): String {
        return "${
            calendar.get(Calendar.DAY_OF_MONTH)
        } ${
            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.getDefault())
        } ${
            calendar.get(Calendar.YEAR)
        }"
    }

    fun getUpToMinutes(calendar: Calendar): String {
        return "${
            calendar.get(Calendar.HOUR)
        }:${
            String.format("%02d", calendar.get(Calendar.MINUTE))            
        }, ${
            calendar.get(Calendar.DAY_OF_MONTH)
        } ${
            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.getDefault())
        } ${
            calendar.get(Calendar.YEAR)
        }"
    }
}

