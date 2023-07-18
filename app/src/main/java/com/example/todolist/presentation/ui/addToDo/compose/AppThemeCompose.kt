package com.example.todolist.presentation.ui.addToDo.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.todolist.R
import com.example.todolist.presentation.ui.addToDo.compose.Color.black
import com.example.todolist.presentation.ui.addToDo.compose.Color.lightBlue
import com.example.todolist.presentation.ui.addToDo.compose.Color.lightGrey
import com.example.todolist.presentation.ui.addToDo.compose.Color.nightBlank
import com.example.todolist.presentation.ui.addToDo.compose.Color.white

@Composable
fun AppThemeCompose(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val lightColorPalette = lightColors(
        primary = colorResource(id = R.color.blue),
        primaryVariant = colorResource(id = R.color.salad),
        secondary = colorResource(id = R.color.blue),
        secondaryVariant = lightBlue,
        background = white,
        surface = colorResource(id = R.color.white),
        error = colorResource(id = R.color.red),
        onBackground = lightGrey,
        onSurface = white,
    )


    val darkColorPalette = darkColors(
        primary = colorResource(id = R.color.blue),
        primaryVariant = colorResource(id = R.color.salad),
        secondary = colorResource(id = R.color.blue),
        secondaryVariant = lightBlue,
        background = black,
        surface = nightBlank,
        error = colorResource(id = R.color.red),
        onBackground = nightBlank,
        onSurface = nightBlank,
    )

    val colors = when {
        darkTheme -> darkColorPalette
        else -> lightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}