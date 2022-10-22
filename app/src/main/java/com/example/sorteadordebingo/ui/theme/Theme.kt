package com.example.sorteadordebingo.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ProjectColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryLight,
    secondary = Secondary,
    secondaryVariant = SecondaryLight,
    background = Surface,
    onPrimary = PrimaryText,
    onSecondary = SecondaryText,
    onBackground = PrimaryText,
    onSurface = PrimaryText,
    surface = Surface
)

@Composable
fun ProjectTheme(
    content: @Composable () -> Unit
) {
    val colors = ProjectColorPalette

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}