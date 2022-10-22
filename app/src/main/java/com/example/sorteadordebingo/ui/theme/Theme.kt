package com.example.sorteadordebingo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = FlowersPrimaryDark,
    primaryVariant = FlowersPrimary,
    onPrimary = Color.White,
    onSurface = Color.White,
    surface = FlowersPrimary
)

@Composable
fun ProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColors() else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}