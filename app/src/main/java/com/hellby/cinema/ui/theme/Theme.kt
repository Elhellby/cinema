package com.hellby.cinema.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    secondary = BrandBlueDark,
    onSecondary = Color.White,
    tertiary = AccentOrange,
    background = Slate100,
    onBackground = Night900,
    surface = Color.White,
    onSurface = Night900,
    surfaceVariant = Slate200,
    onSurfaceVariant = Night900,
    outline = Slate500,
    error = Color(0xFFB00020)
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    secondary = AccentPink,
    onSecondary = Color.White,
    tertiary = AccentOrange,
    background = Night900,
    onBackground = Color.White,
    surface = Night800,
    onSurface = Color.White,
    surfaceVariant = Night800,
    onSurfaceVariant = Slate200,
    outline = Slate500,
    error = Color(0xFFFFB4AB)
)

@Composable
fun CinemaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}