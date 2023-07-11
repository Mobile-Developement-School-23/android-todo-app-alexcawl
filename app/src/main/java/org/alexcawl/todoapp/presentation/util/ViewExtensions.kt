package org.alexcawl.todoapp.presentation.util

import android.app.DatePickerDialog
import android.content.Context
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

fun View.disable() {
    isEnabled = false
}

fun View.snackbar(
    message: String, duration: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, message, duration).show()
}

fun Context.createDatePicker(listener: DatePickerDialog.OnDateSetListener): DatePickerDialog {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return DatePickerDialog(this, R.style.DatePickerDialogTheme, listener, year, month, day)
}

fun Long.toDateFormat(): String = run {
    val instant = Instant.ofEpochMilli(this)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun Priority.toTextFormat(): String =
    this.toString().lowercase().replaceFirstChar { it.uppercase() }

fun createDateString(day: Int, month: Int, year: Int): String =
    String.format("%02d.%02d.%04d", day, month + 1, year)

fun createDateString(calendar: Calendar): String = createDateString(
    calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)
)

fun dateStringToTimestamp(dateString: String): Long {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date = dateFormat.parse(dateString)
    return date?.time ?: 0L
}

val Context.applicationComponent: ApplicationComponent
    get() = when (this) {
        is ToDoApplication -> applicationComponent
        else -> (applicationContext as ToDoApplication).applicationComponent
    }