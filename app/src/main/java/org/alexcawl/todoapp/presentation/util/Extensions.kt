package org.alexcawl.todoapp.presentation.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.di.component.ApplicationComponent
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.presentation.ToDoApplication
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun View.invisible() {
    isVisible = false
}

fun View.gone() {
    isGone = true
}

fun View.snackbar(
    message: String, duration: Int = Snackbar.LENGTH_LONG
) {
    Log.d("SNACKBAR", message)
    Snackbar.make(this, message, duration).show()
}

fun Context.pickDateAndTime(onTimeSet: (Calendar) -> Unit) {
    val currentDateTime = Calendar.getInstance()
    val startYear = currentDateTime.get(Calendar.YEAR)
    val startMonth = currentDateTime.get(Calendar.MONTH)
    val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
    val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
    val startMinute = currentDateTime.get(Calendar.MINUTE)

    DatePickerDialog(this, R.style.DatePickerDialogTheme, { _, year, month, day ->
        TimePickerDialog(this, R.style.DatePickerDialogTheme, { _, hour, minute ->
            val pickedDateTime = Calendar.getInstance()
            pickedDateTime.set(year, month, day, hour, minute)
            onTimeSet(pickedDateTime)
        }, startHour, startMinute, DateFormat.is24HourFormat(this)).show()
    }, startYear, startMonth, startDay).show()
}

fun Long.toDateFormat(): String = run {
    val instant = Instant.ofEpochMilli(this)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, kk:mm"))
}

fun Priority.toTextFormat(): String =
    this.toString().lowercase().replaceFirstChar { it.uppercase() }

fun createDateString(day: Int, month: Int, year: Int, hour: Int, minute: Int): String =
    String.format("%02d.%02d.%04d, %02d:%02d", day, month + 1, year, hour, minute)

fun createDateString(calendar: Calendar): String = createDateString(
    calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY),
    calendar.get(Calendar.MINUTE)
)

fun dateStringToTimestamp(dateString: String): Long {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy, hh:mm", Locale.getDefault())
    val date = dateFormat.parse(dateString)
    return date?.time ?: 0L
}

val Context.applicationComponent: ApplicationComponent
    get() = when (this) {
        is ToDoApplication -> applicationComponent
        else -> (applicationContext as ToDoApplication).applicationComponent
    }

fun UUID.convertToInt(): Int = this.hashCode()