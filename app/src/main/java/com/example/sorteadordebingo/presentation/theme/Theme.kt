package com.example.sorteadordebingo.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColors = lightColors(
    primary = green_800,
    onPrimary = white,
    secondary = green_900,
    onSecondary = white,
    error = light_error,
    onError = white,
    primaryVariant = complementary,
    secondaryVariant = Color.DarkGray
)


private val DarkColors = darkColors(
    primary = green_400,
    onPrimary = black,
    secondary = green_300,
    onSecondary = black,
    error = dark_error,
    onError = black,
    primaryVariant = complementary,
    secondaryVariant = Color.LightGray
)

@Composable
fun AppTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable() () -> Unit
) {
  val colors = if (!useDarkTheme) {
    LightColors
  } else {
    DarkColors
  }

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}