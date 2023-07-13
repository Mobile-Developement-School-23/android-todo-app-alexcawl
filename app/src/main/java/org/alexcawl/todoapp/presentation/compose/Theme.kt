package org.alexcawl.todoapp.presentation.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import org.alexcawl.todoapp.R


@Composable
fun ToDoApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val lightColorPalette = lightColors(
        primary = colorResource(id = R.color.blue),
        primaryVariant = colorResource(id = R.color.blue),
        secondary = colorResource(id = R.color.green),
        secondaryVariant = colorResource(id = R.color.green),
        background = colorResource(id = R.color.lt_back_primary),
        surface = colorResource(id = R.color.lt_back_secondary),
        error = colorResource(id = R.color.red),
        onPrimary = colorResource(id = R.color.lt_label_primary),
        onSecondary = colorResource(id = R.color.lt_label_secondary),
        onBackground = colorResource(id = R.color.gray),
        onSurface = colorResource(id = R.color.lt_label_tertiary),
        onError = colorResource(id = R.color.white)
    )

    val darkColorPalette = darkColors(
        primary = colorResource(id = R.color.blue),
        primaryVariant = colorResource(id = R.color.blue),
        secondary = colorResource(id = R.color.green),
        secondaryVariant = colorResource(id = R.color.green),
        background = colorResource(id = R.color.dt_back_primary),
        surface = colorResource(id = R.color.dt_back_secondary),
        error = colorResource(id = R.color.red),
        onPrimary = colorResource(id = R.color.dt_label_primary),
        onSecondary = colorResource(id = R.color.dt_label_secondary),
        onBackground = colorResource(id = R.color.gray),
        onSurface = colorResource(id = R.color.dt_label_tertiary),
        onError = colorResource(id = R.color.white)
    )

    val colors = when {
        darkTheme -> darkColorPalette
        else -> lightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}