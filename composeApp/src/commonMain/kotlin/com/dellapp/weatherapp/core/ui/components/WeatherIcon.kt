package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dellapp.weatherapp.core.domain.model.WeatherCondition
import org.jetbrains.compose.resources.painterResource
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.cloudy
import weatherapp.composeapp.generated.resources.cloudy_night
import weatherapp.composeapp.generated.resources.moon
import weatherapp.composeapp.generated.resources.rain
import weatherapp.composeapp.generated.resources.snow
import weatherapp.composeapp.generated.resources.sun_rain
import weatherapp.composeapp.generated.resources.sunny
import weatherapp.composeapp.generated.resources.sunny_clouds
import weatherapp.composeapp.generated.resources.tornado

@Composable
fun WeatherIcon(
    weatherCondition: WeatherCondition,
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
) {
    val isNight = weatherCondition.iconCode.endsWith("n")
    val iconResource = when (weatherCondition.id) {
        in 200..232, in 300..321 -> Res.drawable.rain
        in 500..504 -> Res.drawable.sun_rain
        511 -> Res.drawable.snow
        in 520..531 -> Res.drawable.rain
        in 600..622 -> Res.drawable.snow
        781 -> Res.drawable.tornado
        800 -> if (isNight) Res.drawable.moon else Res.drawable.sunny
        801 -> if (isNight) Res.drawable.cloudy_night else Res.drawable.sunny_clouds
        else -> Res.drawable.cloudy
    }

    Icon(
        painter = painterResource(iconResource),
        contentDescription = "Weather: ${weatherCondition.description}",
        modifier = modifier.size(size = size),
        tint = Color.Unspecified
    )
}