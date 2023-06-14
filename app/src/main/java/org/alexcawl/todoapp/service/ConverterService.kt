package org.alexcawl.todoapp.service

class ConverterService private constructor() {
    companion object {
        @Volatile
        private var instance: ConverterService? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ConverterService().also { instance = it }
        }
    }


}

