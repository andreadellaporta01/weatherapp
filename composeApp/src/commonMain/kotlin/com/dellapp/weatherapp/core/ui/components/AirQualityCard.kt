package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dellapp.weatherapp.core.common.CardBorderColor
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.Shapes
import com.dellapp.weatherapp.core.common.TinySpacing
import com.dellapp.weatherapp.core.common.XLargeSpacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.air_quality
import weatherapp.composeapp.generated.resources.air_quality_fair
import weatherapp.composeapp.generated.resources.air_quality_good
import weatherapp.composeapp.generated.resources.air_quality_moderate
import weatherapp.composeapp.generated.resources.air_quality_poor
import weatherapp.composeapp.generated.resources.air_quality_unknown
import weatherapp.composeapp.generated.resources.air_quality_very_poor
import weatherapp.composeapp.generated.resources.ic_air_quality

@Composable
fun AirQualityCard(
    airQuality: Int,
    modifier: Modifier = Modifier,
) {
    val airQualityText = when (airQuality) {
        1 -> stringResource(Res.string.air_quality_good)
        2 -> stringResource(Res.string.air_quality_fair)
        3 -> stringResource(Res.string.air_quality_moderate)
        4 -> stringResource(Res.string.air_quality_poor)
        5 -> stringResource(Res.string.air_quality_very_poor)
        else -> stringResource(Res.string.air_quality_unknown)
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
            modifier = Modifier.padding(XLargeSpacing)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_air_quality),
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
                Spacer(Modifier.width(TinySpacing))
                Text(
                    text = stringResource(Res.string.air_quality),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                )
            }
            Spacer(Modifier.height(MediumSpacing))
            Text(
                text = "$airQuality-$airQualityText",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp)
            )
            Spacer(Modifier.height(MediumSpacing))
            GradientDisplaySlider(value = airQuality)
        }
    }

}