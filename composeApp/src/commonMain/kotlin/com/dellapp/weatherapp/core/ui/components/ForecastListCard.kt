package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dellapp.weatherapp.core.common.EndForecastGradientBg
import com.dellapp.weatherapp.core.common.ForecastType
import com.dellapp.weatherapp.core.common.Language
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.Shapes
import com.dellapp.weatherapp.core.common.StartForecastGradientBg
import com.dellapp.weatherapp.core.common.XXLargeSpacing
import com.dellapp.weatherapp.core.domain.model.WeatherInfo
import org.jetbrains.compose.resources.stringResource
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.hourly_forecast
import weatherapp.composeapp.generated.resources.weekly_forecast

@Composable
fun ForecastListCard(
    hourlyWeatherInfos: List<WeatherInfo> = listOf(),
    weeklyWeatherInfos: List<WeatherInfo> = listOf(),
    isLoading: Boolean = false,
    language: Language,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    var selectedForecast by remember { mutableStateOf(ForecastType.HOURLY) }

    GradientBox(
        colors = listOf(StartForecastGradientBg, EndForecastGradientBg),
        gradientType = GradientType.RADIAL,
        modifier = modifier.clip(Shapes.extraLarge).fillMaxWidth(),
        paddingValues = paddingValues
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center).padding(vertical = XXLargeSpacing)
            )
            return@GradientBox
        }

        Column {
            ForecastTabSwitcher(
                selectedTab = selectedForecast,
                onTabSelected = { selectedForecast = it }
            )
            Spacer(Modifier.height(20.dp))
            when (selectedForecast) {
                ForecastType.HOURLY -> {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(LargeSpacing),
                        contentPadding = PaddingValues(horizontal = MediumSpacing)
                    ) {
                        items(hourlyWeatherInfos.size) { position ->
                            ForecastCard(
                                weatherInfo = hourlyWeatherInfos[position],
                                isHourly = true,
                            )
                        }
                    }
                }

                ForecastType.WEEKLY -> {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(LargeSpacing),
                        contentPadding = PaddingValues(horizontal = MediumSpacing)
                    ) {
                        items(weeklyWeatherInfos.size) { position ->
                            ForecastCard(
                                weatherInfo = weeklyWeatherInfos[position],
                                language = language
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun ForecastTabSwitcher(
    selectedTab: ForecastType,
    onTabSelected: (ForecastType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ForecastTab(
            text = stringResource(Res.string.hourly_forecast),
            isSelected = selectedTab == ForecastType.HOURLY,
            onClick = { onTabSelected(ForecastType.HOURLY) },
        )

        ForecastTab(
            text = stringResource(Res.string.weekly_forecast),
            isSelected = selectedTab == ForecastType.WEEKLY,
            onClick = { onTabSelected(ForecastType.WEEKLY) },
        )
    }
}

@Composable
fun ForecastTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}