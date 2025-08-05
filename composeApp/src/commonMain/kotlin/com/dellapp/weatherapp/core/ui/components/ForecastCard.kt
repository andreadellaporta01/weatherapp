package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dellapp.weatherapp.core.common.Cian
import com.dellapp.weatherapp.core.common.Language
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.SmallSpacing
import com.dellapp.weatherapp.core.common.XXLargeSpacing
import com.dellapp.weatherapp.core.domain.model.WeatherInfo

@Composable
fun ForecastCard(
    weatherInfo: WeatherInfo,
    isHourly: Boolean = false,
    language: Language? = null,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge.copy(all = CornerSize(XXLargeSpacing)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.2f),
        ),
        border = BorderStroke(width = 1.dp, color = Color.White.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            modifier = modifier.widthIn(min = 60.dp)
                .padding(horizontal = SmallSpacing, vertical = LargeSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isHourly) {
                    weatherInfo.getFormattedTime()
                } else {
                    weatherInfo.getFormattedDay(
                        language = language!!.iso
                    )
                },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(Modifier.height(MediumSpacing))

            if (weatherInfo.weatherCondition != null) {
                WeatherIcon(
                    weatherCondition = weatherInfo.weatherCondition,
                )
            }

            if (weatherInfo.precipitationProb > 30) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = weatherInfo.precipitationProb.toString(),
                    color = Cian,
                    style = MaterialTheme.typography.labelMedium,
                )
            }

            Spacer(Modifier.height(MediumSpacing))

            Text(
                text = weatherInfo.getFormattedTemperature(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}