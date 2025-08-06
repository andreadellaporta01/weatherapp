package com.dellapp.weatherapp.core.ui.components

import AnimatedSunCurveComponent
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
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.Shapes
import com.dellapp.weatherapp.core.common.TinySpacing
import com.dellapp.weatherapp.core.common.XLargeSpacing
import com.dellapp.weatherapp.core.common.formatToAmPm
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import weatherapp.composeapp.generated.icSunrise
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.sunrise
import weatherapp.composeapp.generated.resources.sunset

@Composable
fun SunPathCard(
    sunrise: LocalDateTime,
    sunset: LocalDateTime,
    modifier: Modifier = Modifier,
) {
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
                SvgImage(
                    image = icSunrise,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
                Spacer(Modifier.width(TinySpacing))
                Text(
                    text = stringResource(Res.string.sunrise),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                )
            }
            Spacer(Modifier.height(MediumSpacing))
            Text(
                text = sunrise.formatToAmPm(withMinutes = true),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp)
            )
            AnimatedSunCurveComponent(sunrise.time, sunset.time)
            Text(
                text = stringResource(Res.string.sunset, sunset.formatToAmPm(withMinutes = true)),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 12.sp)
            )
        }
    }

}