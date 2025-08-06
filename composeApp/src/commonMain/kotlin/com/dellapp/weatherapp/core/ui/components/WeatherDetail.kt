package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dellapp.weatherapp.core.common.Language
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.XXLargeSpacing
import com.dellapp.weatherapp.core.common.getScreenPaddingValues
import com.dellapp.weatherapp.core.domain.model.Weather
import org.jetbrains.compose.resources.stringResource
import weatherapp.composeapp.generated.icEye
import weatherapp.composeapp.generated.icHumidity
import weatherapp.composeapp.generated.icPressure
import weatherapp.composeapp.generated.icRain
import weatherapp.composeapp.generated.icTemperature
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.dew_point_description
import weatherapp.composeapp.generated.resources.feels_like
import weatherapp.composeapp.generated.resources.feels_like_description
import weatherapp.composeapp.generated.resources.humidity
import weatherapp.composeapp.generated.resources.in_last_hour
import weatherapp.composeapp.generated.resources.precipitation_description
import weatherapp.composeapp.generated.resources.pressure
import weatherapp.composeapp.generated.resources.pressure_description
import weatherapp.composeapp.generated.resources.rainfall
import weatherapp.composeapp.generated.resources.rainfall_mm
import weatherapp.composeapp.generated.resources.visibility
import weatherapp.composeapp.generated.resources.visibility_description

@Composable
fun WeatherDetail(
    weather: Weather?,
    selectedLanguage: Language
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(getScreenPaddingValues())
    ) {
        Spacer(modifier = Modifier.height(MediumSpacing))
        Text(
            text = weather?.cityName.orEmpty(),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "${
                weather?.currentWeather?.getTemperatureFormatted()
                    .orEmpty()
            } | ${weather?.currentWeather?.weatherCondition?.description.orEmpty()}",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(Modifier.height(XXLargeSpacing))

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                bottom = 100.dp
            ),
        ) {
            item {
                ForecastListCard(
                    hourlyWeatherInfos = weather?.hourlyForecast.orEmpty(),
                    weeklyWeatherInfos = weather?.dailyForecast.orEmpty(),
                    isLoading = weather == null,
                    paddingValues = PaddingValues(top = 20.dp),
                    language = selectedLanguage
                )
            }

            item { Spacer(Modifier.height(LargeSpacing)) }

            item {
                AirQualityCard(
                    airQuality = weather?.airQuality ?: 0
                )
            }

            item { Spacer(Modifier.height(MediumSpacing)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    UVCard(
                        uvi = weather?.currentWeather?.uvi ?: 0.0,
                        modifier = Modifier.weight(1f).height(220.dp)
                    )
                    Spacer(Modifier.width(MediumSpacing))
                    if (weather?.currentWeather?.sunrise != null) {
                        SunPathCard(
                            sunrise = weather.currentWeather.sunrise,
                            sunset = weather.currentWeather.sunset,
                            modifier = Modifier.weight(1f).height(220.dp)
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(MediumSpacing)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    WindCard(
                        windAngle = weather?.currentWeather?.windAngle ?: 0,
                        windSpeed = weather?.currentWeather?.windSpeed ?: 0.0,
                        modifier = Modifier.weight(1f).height(200.dp)
                    )
                    Spacer(Modifier.width(MediumSpacing))
                    GeneralInfoCard(
                        icon = icRain,
                        name = stringResource(Res.string.rainfall),
                        title = stringResource(
                            Res.string.rainfall_mm,
                            weather?.currentWeather?.rain ?: 0.0
                        ),
                        subtitle = stringResource(
                            Res.string.in_last_hour,
                        ),
                        description = stringResource(
                            Res.string.precipitation_description,
                        ),
                        modifier = Modifier.weight(1f).height(200.dp)
                    )
                }
            }

            item { Spacer(Modifier.height(MediumSpacing)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GeneralInfoCard(
                        icon = icTemperature,
                        name = stringResource(Res.string.feels_like),
                        title = weather?.currentWeather?.getFeelsLikeFormatted().orEmpty(),
                        description = stringResource(Res.string.feels_like_description),
                        modifier = Modifier.weight(1f).height(200.dp)
                    )
                    Spacer(Modifier.width(MediumSpacing))
                    GeneralInfoCard(
                        icon = icHumidity,
                        name = stringResource(Res.string.humidity),
                        title = weather?.currentWeather?.getHumidityFormatted().orEmpty(),
                        description = stringResource(
                            Res.string.dew_point_description,
                            weather?.currentWeather?.dewPoint ?: 0.0
                        ),
                        modifier = Modifier.weight(1f).height(200.dp)
                    )
                }
            }

            item { Spacer(Modifier.height(MediumSpacing)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GeneralInfoCard(
                        icon = icEye,
                        name = stringResource(Res.string.visibility),
                        title = weather?.currentWeather?.getVisibilityFormatted().orEmpty(),
                        description = stringResource(Res.string.visibility_description),
                        modifier = Modifier.weight(1f).height(200.dp)
                    )
                    Spacer(Modifier.width(MediumSpacing))
                    GeneralInfoCard(
                        icon = icPressure,
                        name = stringResource(Res.string.pressure),
                        title = weather?.currentWeather?.getPressureFormatted().orEmpty(),
                        description = stringResource(Res.string.pressure_description),
                        modifier = Modifier.weight(1f).height(200.dp)
                    )
                }
            }
        }
    }
}