package org.alexcawl.todoapp.presentation.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.data.repository.TaskLocalRepository
import org.alexcawl.todoapp.presentation.util.applicationComponent
import org.alexcawl.todoapp.presentation.util.convertToInt
import java.util.*
import javax.inject.Inject

class PostponeReceiver : BroadcastReceiver() {
    private companion object {
        const val ID = AlarmScheduler.ID
        const val DAY_IN_MILLISECONDS = 86400000L
    }

    @Inject
    lateinit var coroutineScope: CoroutineScope

    @Inject
    lateinit var repository: TaskLocalRepository

    override fun onReceive(context: Context, intent: Intent) {
        context.applicationComponent.inject(this)
        try {
            coroutineScope.launch {
                val id: UUID = getID(intent.extras)
                when (val task = repository.getTaskAsModel(id)) {
                    null -> {}
                    else -> {
                        val manager =
                            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        when (val deadline = task.deadline) {
                            null -> {}
                            else -> {
                                try {
                                    repository.updateTask(task.copy(deadline = deadline + DAY_IN_MILLISECONDS))
                                    manager.cancel(task.id.convertToInt())
                                } catch (exception: Exception) {
                                    Log.d(
                                        this::class.java.toString(),
                                        exception.stackTraceToString()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (exception: Exception) {
            Log.d(this::class.java.toString(), exception.stackTraceToString())
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getID(arguments: Bundle?): UUID {
        val uuid = arguments?.getString(ID) ?: throw IllegalArgumentException("Null UUID!")
        return UUID.fromString(uuid) ?: throw IllegalArgumentException("Non-valid UUID: $uuid")
    }
}