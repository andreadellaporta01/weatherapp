package com.dellapp.weatherapp.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    @SerialName("current")
    val current: Current? = null,
    @SerialName("daily")
    val daily: List<Daily>? = null,
    @SerialName("hourly")
    val hourly: List<Hourly>? = null,
    @SerialName("lat")
    val lat: Double? = null,
    @SerialName("lon")
    val lon: Double? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("timezone_offset")
    val timezoneOffset: Int? = null
)

@Serializable
data class Current(
    @SerialName("clouds")
    val clouds: Int? = null,
    @SerialName("dew_point")
    val dewPoint: Double? = null,
    @SerialName("dt")
    val dt: Int? = null,
    @SerialName("feels_like")
    val feelsLike: Double? = null,
    @SerialName("humidity")
    val humidity: Int? = null,
    @SerialName("pressure")
    val pressure: Int? = null,
    @SerialName("sunrise")
    val sunrise: Int? = null,
    @SerialName("sunset")
    val sunset: Int? = null,
    @SerialName("temp")
    val temp: Double? = null,
    @SerialName("uvi")
    val uvi: Double? = null,
    @SerialName("visibility")
    val visibility: Int? = null,
    @SerialName("weather")
    val weather: List<Weather?>? = null,
    @SerialName("wind_deg")
    val windDeg: Int? = null,
    @SerialName("wind_gust")
    val windGust: Double? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null
)

@Serializable
data class Daily(
    @SerialName("clouds")
    val clouds: Int? = null,
    @SerialName("dew_point")
    val dewPoint: Double? = null,
    @SerialName("dt")
    val dt: Int? = null,
    @SerialName("feels_like")
    val feelsLike: FeelsLike? = null,
    @SerialName("humidity")
    val humidity: Int? = null,
    @SerialName("moon_phase")
    val moonPhase: Double? = null,
    @SerialName("moonrise")
    val moonrise: Int? = null,
    @SerialName("moonset")
    val moonset: Int? = null,
    @SerialName("pop")
    val pop: Double? = null,
    @SerialName("pressure")
    val pressure: Int? = null,
    @SerialName("rain")
    val rain: Double? = null,
    @SerialName("summary")
    val summary: String? = null,
    @SerialName("sunrise")
    val sunrise: Int? = null,
    @SerialName("sunset")
    val sunset: Int? = null,
    @SerialName("temp")
    val temp: Temp? = null,
    @SerialName("uvi")
    val uvi: Double? = null,
    @SerialName("weather")
    val weather: List<Weather>? = null,
    @SerialName("wind_deg")
    val windDeg: Int? = null,
    @SerialName("wind_gust")
    val windGust: Double? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null
)

@Serializable
data class Hourly(
    @SerialName("clouds")
    val clouds: Int? = null,
    @SerialName("dew_point")
    val dewPoint: Double? = null,
    @SerialName("dt")
    val dt: Int? = null,
    @SerialName("feels_like")
    val feelsLike: Double? = null,
    @SerialName("humidity")
    val humidity: Int? = null,
    @SerialName("pop")
    val pop: Double? = null,
    @SerialName("pressure")
    val pressure: Int? = null,
    @SerialName("temp")
    val temp: Double? = null,
    @SerialName("uvi")
    val uvi: Double? = null,
    @SerialName("visibility")
    val visibility: Int? = null,
    @SerialName("weather")
    val weather: List<Weather>? = null,
    @SerialName("wind_deg")
    val windDeg: Int? = null,
    @SerialName("wind_gust")
    val windGust: Double? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null
)

@Serializable
data class Weather(
    @SerialName("description")
    val description: String? = null,
    @SerialName("icon")
    val icon: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("main")
    val main: String? = null
)

@Serializable
data class FeelsLike(
    @SerialName("day")
    val day: Double? = null,
    @SerialName("eve")
    val eve: Double? = null,
    @SerialName("morn")
    val morn: Double? = null,
    @SerialName("night")
    val night: Double? = null
)

@Serializable
data class Temp(
    @SerialName("day")
    val day: Double? = null,
    @SerialName("eve")
    val eve: Double? = null,
    @SerialName("max")
    val max: Double? = null,
    @SerialName("min")
    val min: Double? = null,
    @SerialName("morn")
    val morn: Double? = null,
    @SerialName("night")
    val night: Double? = null
)
