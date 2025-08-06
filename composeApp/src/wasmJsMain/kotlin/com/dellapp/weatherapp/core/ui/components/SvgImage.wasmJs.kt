package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import weatherapp.composeapp.generated.drawableMap

@Composable
actual fun SvgImage(
    image: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    colorFilter: ColorFilter?,
) {
    val painter = painterResource(drawableMap[image]!!)
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        colorFilter = colorFilter
    )
}