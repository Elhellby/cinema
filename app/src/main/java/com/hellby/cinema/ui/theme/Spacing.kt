package com.hellby.cinema.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Escala de espaciados consistente para toda la app. */
data class Spacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 20.dp,
    val xxl: Dp = 24.dp,
    val cardCornerRadius: Dp = 16.dp,
    val carouselCornerRadius: Dp = 28.dp,
    val posterWidth: Dp = 170.dp,
    val posterHeight: Dp = 220.dp,
    val carouselHeight: Dp = 440.dp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

val MaterialSpacing: Spacing
    @Composable
    get() = LocalSpacing.current
