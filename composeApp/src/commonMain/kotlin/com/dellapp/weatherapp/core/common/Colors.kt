package com.dellapp.weatherapp.core.common

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Light Theme Colors

val LightPrimary = Color(0xFF000000)
val LightSecondary = Color(0xFF000000).copy(alpha = 0.7f)
val LightGrayTertiary = Color(0xFF000000).copy(alpha = 0.5f)
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFFFFFFF)
val LightTextColor = Color(0xFF333333)
val LightStartForecastGradientBg = Color(0xFFB0C5E5)
val LightEndForecastGradientBg = Color(0xFFFFFFFF)

// Dark Theme Colors
val DarkPrimary = Color(0xFFFFFFFF)
val DarkSecondary = Color(0xFFFFFFFF).copy(alpha = 0.7f)
val DarkTertiary = Color(0xFFFFFFFF).copy(alpha = 0.5f)
val DarkBackground = Color(0xFF48319D)
val DarkSurface = Color(0xFF2A2A2A)
val DarkTextColor = Color(0xFFE0E0E0)
val DarkStartForecastGradientBg = Color(0xFF422E5A)
val DarkEndForecastGradientBg = Color(0xFF1C1B33)

// Common Colors
val Cian = Color(0xFF32ADE6)
val StartGradientBg = Color(0xFF2E335A)
val EndGradientBg = Color(0xFF1C1B33)
val CardBorderColor = Color(0xFFC427FB).copy(alpha = 0.4f)
val StartSliderGradientBg = Color(0xFF3758B1)
val EndSliderGradientBg = Color(0xFFE64395)

internal val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    background = LightBackground,
    onBackground = LightTextColor,
    surface = LightSurface,
    onSurface = LightPrimary,
    secondary = LightSecondary,
    onSecondary = LightTextColor,
    tertiary = LightGrayTertiary,
    surfaceContainerHigh = LightStartForecastGradientBg,
    surfaceContainerLow = LightEndForecastGradientBg
)

internal val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    background = DarkBackground,
    onBackground = DarkTextColor,
    surface = DarkSurface,
    onSurface = DarkPrimary,
    secondary = DarkSecondary,
    onSecondary = DarkTextColor,
    tertiary = DarkTertiary,
    surfaceContainerHigh = DarkStartForecastGradientBg,
    surfaceContainerLow = DarkEndForecastGradientBg
)