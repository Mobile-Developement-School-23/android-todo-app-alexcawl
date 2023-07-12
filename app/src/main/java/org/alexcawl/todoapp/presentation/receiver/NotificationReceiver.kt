package org.alexcawl.todoapp.presentation.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.data.repository.TaskLocalRepository
import org.alexcawl.todoapp.domain.repository.IAlarmScheduler
import org.alexcawl.todoapp.presentation.util.applicationComponent
import org.alexcawl.todoapp.presentation.util.convertToInt
import org.alexcawl.todoapp.presentation.util.toTextFormat
import java.util.*
import javax.inject.Inject

class NotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var coroutineScope: CoroutineScope

    @Inject
    lateinit var scheduler: IAlarmScheduler

    @Inject
    lateinit var repository: TaskLocalRepository

    private companion object {
        const val CHANNEL_ID = "TODO-NOTIFY"
        const val CHANNEL_NAME = "ToDo Notification"
        const val ID = AlarmScheduler.ID
    }

    override fun onReceive(context: Context, intent: Intent) {
        println(System.currentTimeMillis())
        context.applicationComponent.inject(this)
        try {
            coroutineScope.launch {
                val id: UUID = getID(intent.extras)
                when (val task = repository.getTaskAsModel(id)) {
                    null -> {}
                    else -> {
                        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.createNotificationChannel(buildChannel())
                        val notification = buildNotification(
                            context,
                            R.drawable.icon_check,
                            "Priority ${task.priority.toTextFormat()}",
                            "${task.text.padEnd(20, ' ').substring(0, 20).trim()}...",
                            buildNavigationIntent(context, id),
                            buildPostponeIntent(context, id)
                        )
                        scheduler.cancelNotification(task)
                        manager.notify(id.convertToInt(), notification)
                    }
                }
            }
        } catch (exception: Exception) {
            Log.d(this::class.java.toString(), exception.stackTraceToString())
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getID(arguments: Bundle?): UUID {
        val uuid = arguments?.getString(ID)
            ?: throw IllegalArgumentException("Null UUID!")
        return UUID.fromString(uuid)
            ?: throw IllegalArgumentException("Non-valid UUID: $uuid")
    }

    private fun buildNavigationIntent(context: Context, id: UUID): PendingIntent =
        NavDeepLinkBuilder(context)
            .setGraph(R.navigation.navigation_graph)
            .setDestination(R.id.taskShowFragment)
            .setArguments(bundleOf("UUID" to id.toString()))
            .createPendingIntent()

    private fun buildPostponeIntent(context: Context, id: UUID): PendingIntent {
        return PendingIntent.getBroadcast(
            context, id.convertToInt(),
            Intent(context, PostponeReceiver::class.java).apply {
                putExtra(ID, id.toString())
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildNotification(
        context: Context,
        icon: Int,
        title: String,
        text: String,
        contentIntent: PendingIntent,
        deferringIntent: PendingIntent
    ): Notification = Notification.Builder(context, CHANNEL_ID)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(text)
        .setContentIntent(contentIntent)
        .setAutoCancel(true)
        .addAction(
            Notification.Action.Builder(R.drawable.icon_clear, "Postpone", deferringIntent).build()
        )
        .build()

    private fun buildChannel() = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
    )
}

























