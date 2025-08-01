package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class GradientType {
    LINEAR, RADIAL, SWEEP
}

@Composable
fun GradientBox(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    gradientType: GradientType = GradientType.LINEAR,
    radius: Float = 500f,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val brush = when (gradientType) {
        GradientType.LINEAR -> Brush.linearGradient(colors)
        GradientType.RADIAL -> Brush.radialGradient(colors = colors, radius = radius)
        GradientType.SWEEP -> Brush.sweepGradient(colors)
    }

    Box(
        modifier = modifier.background(brush = brush)
            .padding(paddingValues),
        content = content,
        contentAlignment = contentAlignment
    )
}
