package org.alexcawl.todoapp.presentation.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.IAlarmScheduler
import org.alexcawl.todoapp.presentation.util.convertToInt
import javax.inject.Inject

class AlarmScheduler @Inject constructor(
    private val context: Context
): IAlarmScheduler {
    private val manager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val ID = "UUID"
    }

    override fun scheduleNotification(task: TaskModel) {
        when (val deadline = task.deadline) {
            null -> {}
            else -> {
                val intent = Intent(context, NotificationReceiver::class.java).apply {
                    putExtra(ID, task.id.toString())
                }
                val pendingID = task.id.convertToInt()
                val pending = PendingIntent.getBroadcast(
                    context,
                    pendingID,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, deadline, pending)
            }
        }
    }

    override fun cancelNotification(task: TaskModel) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingID = task.id.convertToInt()
        val pending = PendingIntent.getBroadcast(
            context,
            pendingID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        manager.cancel(pending)
    }
}