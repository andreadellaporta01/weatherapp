package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dellapp.weatherapp.core.common.CardBorderColor
import com.dellapp.weatherapp.core.common.Shapes
import com.dellapp.weatherapp.core.common.TinySpacing
import com.dellapp.weatherapp.core.common.XLargeSpacing
import org.jetbrains.compose.resources.stringResource
import weatherapp.composeapp.generated.icSunBrightness
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.high
import weatherapp.composeapp.generated.resources.low
import weatherapp.composeapp.generated.resources.moderate
import weatherapp.composeapp.generated.resources.unknown
import weatherapp.composeapp.generated.resources.uv_index
import weatherapp.composeapp.generated.resources.very_high

@Composable
fun UVCard(
    uvi: Double,
    modifier: Modifier = Modifier,
) {
    val uviText = when (uvi) {
        in 0.0..2.0 -> stringResource(Res.string.low)
        in 3.0..5.0 -> stringResource(Res.string.moderate)
        in 6.0..7.0 -> stringResource(Res.string.high)
        in 8.0..10.0 -> stringResource(Res.string.very_high)
        else -> stringResource(Res.string.unknown)
    }

    Card(
        modifier = modifier,
        shape = Shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 2.dp,
            color = CardBorderColor.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxHeight().padding(XLargeSpacing),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SvgImage(
                    image = icSunBrightness,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
                Spacer(Modifier.width(TinySpacing))
                Text(
                    text = stringResource(Res.string.uv_index),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                )
            }
            Column {
                Text(
                    text = "$uvi",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp)
                )
                Text(
                    text = uviText,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp)
                )
            }
            GradientDisplaySlider(value = uvi.toInt(), max = 10)
        }
    }

}