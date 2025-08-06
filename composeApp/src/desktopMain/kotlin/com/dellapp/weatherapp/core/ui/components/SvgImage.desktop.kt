package com.dellapp.weatherapp.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import weatherapp.composeapp.generated.resources.Res

@Composable
actual fun SvgImage(
    image: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    colorFilter: ColorFilter?,
) {
    AsyncImage(
        model = Res.getUri("drawable/$image.svg"),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        colorFilter = colorFilter
    )
}