package org.alexcawl.todoapp.presentation.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    val colors = when (darkTheme) {
        true -> darkColorPalette
        false -> lightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Preview(name = "Light Theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ThemePreview() {
    ToDoApplicationTheme {
        ThemeCanvas()
    }
}

@Composable
fun ThemeCanvas() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Text(
                text = "Primary",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.primary)
            )
            Text(
                text = "PrimaryVariant",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.primaryVariant)
            )
            Text(
                text = "OnPrimary",
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.onPrimary)
            )
        }
        Row {
            Text(
                text = "Secondary",
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Text(
                text = "SecondaryVariant",
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.secondaryVariant)
            )
            Text(
                text = "OnSecondary",
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.onSecondary)
            )
        }
        Row {
            Text(
                text = "Background",
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.background)
            )
            Text(
                text = "OnBackground",
                color = MaterialTheme.colors.background,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.onBackground)
            )
        }
        Row {
            Text(
                text = "Surface",
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.surface)
            )
            Text(
                text = "OnSurface",
                color = MaterialTheme.colors.surface,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.onSurface)
            )
        }
        Row {
            Text(
                text = "Error",
                color = MaterialTheme.colors.onError,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.error)
            )
            Text(
                text = "OnError",
                color = MaterialTheme.colors.error,
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colors.onError)
            )
        }
        Column(
            modifier = Modifier.background(color = MaterialTheme.colors.surface),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "h1", style = MaterialTheme.typography.h1, color = MaterialTheme.colors.onSurface)
            Text(text = "subtitle1", style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.onSurface)
            Text(text = "button", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onSurface)
            Text(text = "body1", style = MaterialTheme.typography.body1, color = MaterialTheme.colors.onSurface)
            Text(text = "body2", style = MaterialTheme.typography.body2, color = MaterialTheme.colors.onSurface)
        }
    }
}