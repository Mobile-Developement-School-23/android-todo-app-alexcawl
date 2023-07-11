package org.alexcawl.todoapp.domain.repository

import org.alexcawl.todoapp.domain.model.TaskModel

interface IAlarmScheduler {
    fun scheduleNotification(task: TaskModel)

    fun cancelNotification(task: TaskModel)
}