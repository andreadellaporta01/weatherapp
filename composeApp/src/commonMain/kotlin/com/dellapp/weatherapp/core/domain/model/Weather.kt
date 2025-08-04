package com.dellapp.weatherapp.core.domain.model

import com.dellapp.weatherapp.core.common.formatToAmPm
import com.dellapp.weatherapp.core.common.localizedShortName
import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class Weather(
    val cityName: String,
    val currentWeather: CurrentWeather,
    val hourlyForecast: List<WeatherInfo>,
    val dailyForecast: List<WeatherInfo>,
    val airQuality: Int? = null
)

data class CurrentWeather(
    val temperature: Double,
    val minTemperature: Double,
    val maxTemperature: Double,
    val weatherCondition: WeatherCondition,
    val uvi: Double,
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime,
    val windSpeed: Double,
    val windAngle: Int,
    val rain: Double,
    val feelsLike: Double,
    val humidity: Int,
    val visibility: Int,
    val pressure: Int,
    val dewPoint: Double
) {
    fun getTemperatureFormatted(): String = "${temperature.toInt()}°"
    fun getFeelsLikeFormatted(): String = "${feelsLike.toInt()}°"
    fun getMinFormatted(): String = "${minTemperature.toInt()}°"
    fun getMaxFormatted(): String = "${maxTemperature.toInt()}°"
    fun getHumidityFormatted(): String = "$humidity%"
    fun getVisibilityFormatted(): String = "$visibility km"
    fun getPressureFormatted(): String = "$pressure hPa"
}

data class WeatherInfo(
    val dateTime: LocalDateTime,
    val weatherCondition: WeatherCondition?,
    val precipitationProb: Double,
    val temperature: Double
) {
    fun getFormattedTemperature(): String = "${temperature.toInt()}°"
    fun getFormattedTime(): String = dateTime.formatToAmPm()
    fun getFormattedDay(language: String): String = dateTime.date.dayOfWeek.localizedShortName(language)
}

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val iconCode: String
)