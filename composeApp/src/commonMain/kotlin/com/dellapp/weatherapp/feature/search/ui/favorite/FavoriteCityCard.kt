package com.dellapp.weatherapp.feature.search.ui.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.ThemeStyle
import com.dellapp.weatherapp.core.common.TinySpacing
import com.dellapp.weatherapp.core.common.XXLargeSpacing
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.core.ui.CoreViewModel
import com.dellapp.weatherapp.core.ui.components.SvgImage
import com.dellapp.weatherapp.core.ui.components.WeatherIcon
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import weatherapp.composeapp.generated.bgWeatherCardDark
import weatherapp.composeapp.generated.bgWeatherCardLight
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.favorite_city_card_high_temp
import weatherapp.composeapp.generated.resources.favorite_city_card_low_temp
import weatherapp.composeapp.generated.resources.refresh

@Composable
fun FavoriteCityCard(
    city: City,
    onCitySelected: (City) -> Unit,
    viewModel: FavoriteCityViewModel = koinViewModel(key = "${city.lat},${city.lon}")
) {
    val coreViewModel: CoreViewModel = getKoin().get()
    val uiState by viewModel.uiState.collectAsState()
    val isSystemDark = isSystemInDarkTheme()
    val themeStyle = coreViewModel.selectedThemeStyle.value
        ?: (if (isSystemDark) ThemeStyle.Dark else ThemeStyle.Light)

    LaunchedEffect(Unit) {
        viewModel.getCurrentWeather(city)
    }

    Card(
        modifier = Modifier.clickable(onClick = {
            onCitySelected(city)
        }),
        shape = MaterialTheme.shapes.extraLarge.copy(all = CornerSize(XXLargeSpacing)),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
    ) {
        Box(
            modifier = Modifier.height(height = 184.dp)
        ) {
            SvgImage(
                image = if (themeStyle == ThemeStyle.Dark) {
                    bgWeatherCardDark
                } else {
                    bgWeatherCardLight
                },
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize().align(Alignment.BottomStart)
            )
            Box(
                modifier = Modifier.matchParentSize()
                    .padding(start = LargeSpacing, end = LargeSpacing, bottom = LargeSpacing)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (!uiState.error.isNullOrEmpty()) {
                    Text(
                        text = uiState.error.orEmpty(),
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(Res.string.refresh),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .clickable {
                                viewModel.getCurrentWeather(city)
                            }
                    )

                    Column(
                        modifier = Modifier.align(Alignment.BottomStart)
                    ) {
                        Text(
                            text = uiState.weather?.currentWeather?.getTemperatureFormatted()
                                .orEmpty(),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 64.sp)
                        )
                        Spacer(modifier = Modifier.height(MediumSpacing))
                        Row {
                            Text(
                                text = stringResource(
                                    Res.string.favorite_city_card_high_temp,
                                    uiState.weather?.currentWeather?.getMaxFormatted().orEmpty()
                                ),
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
                            )
                            Spacer(modifier = Modifier.width(MediumSpacing))
                            Text(
                                text = stringResource(
                                    Res.string.favorite_city_card_low_temp,
                                    uiState.weather?.currentWeather?.getMinFormatted().orEmpty()
                                ),
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
                            )
                        }
                        Spacer(modifier = Modifier.height(TinySpacing))
                        Text(
                            text = uiState.weather?.cityName.orEmpty(),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    uiState.weather?.currentWeather?.weatherCondition?.let { weatherCondition ->
                        WeatherIcon(
                            weatherCondition = weatherCondition,
                            modifier = Modifier.align(Alignment.TopEnd),
                            size = 140.dp
                        )
                    }
                }
            }
        }
    }
}