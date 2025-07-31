package com.dellapp.weatherapp.core.common

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Light Theme Colors
val BlackPrimary = Color(0xFF000000)
val LightSecondary = Color(0xFF3C3C43).copy(alpha = 0.6f)
val LightGrayTertiary = Color(0xFF3C3C43).copy(alpha = 0.3f)
val LightBackground = Color(0xFFF8F8F8)
val LightSurface = Color(0xFFFFFFFF)
val LightTextColor = Color(0xFF333333)

// Dark Theme Colors
val WhitePrimary = Color(0xFFFFFFFF)
val DarkSecondary = Color(0xFFEBEBF5).copy(alpha = 0.6f)
val DarkTertiary = Color(0xFFEBEBF5).copy(alpha = 0.3f)
val DarkBackground = Color(0xFF48319D)
val DarkSurface = Color(0xFF2A2A2A)
val DarkTextColor = Color(0xFFE0E0E0)

// Common Colors
val Cian = Color(0xFF32ADE6)
val StartGradientBg = Color(0xFF2E335A)
val EndGradientBg = Color(0xFF1C1B33)

val StartForecastGradientBg = Color(0xFF45278B)
val EndForecastGradientBg = Color(0xFF2E335A)

internal val LightColorScheme = lightColorScheme(
    primary = BlackPrimary,
    background = LightBackground,
    onBackground = LightTextColor,
    surface = LightSurface,
    onSurface = BlackPrimary,
    secondary = LightSecondary,
    onSecondary = LightTextColor,
    tertiary = LightGrayTertiary,
)

internal val DarkColorScheme = darkColorScheme(
    primary = WhitePrimary,
    background = DarkBackground,
    onBackground = DarkTextColor,
    surface = DarkSurface,
    onSurface = WhitePrimary,
    secondary = DarkSecondary,
    onSecondary = DarkTextColor,
    tertiary = DarkTertiary
)