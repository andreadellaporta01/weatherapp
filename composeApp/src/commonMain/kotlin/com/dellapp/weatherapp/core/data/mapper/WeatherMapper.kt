package com.dellapp.weatherapp.core.data.mapper

import CityDto
import com.dellapp.weatherapp.core.common.parseTimestampSecondsToLocalDateTime
import com.dellapp.weatherapp.core.data.dto.WeatherDto
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.core.domain.model.CurrentWeather
import com.dellapp.weatherapp.core.domain.model.Weather
import com.dellapp.weatherapp.core.domain.model.WeatherCondition
import com.dellapp.weatherapp.core.domain.model.WeatherInfo

class WeatherMapper {
    fun mapToWeather(dto: WeatherDto): Weather {
        return Weather(
            currentWeather = CurrentWeather(
                temperature = dto.current?.temp ?: 0.0,
                minTemperature = dto.daily?.firstOrNull()?.temp?.min ?: 0.0,
                maxTemperature = dto.daily?.firstOrNull()?.temp?.max ?: 0.0,
                weatherCondition = WeatherCondition(
                    dto.current?.weather?.firstOrNull()?.id ?: 0,
                    dto.current?.weather?.firstOrNull()?.main.orEmpty(),
                    dto.current?.weather?.firstOrNull()?.description.orEmpty(),
                    dto.current?.weather?.firstOrNull()?.icon.orEmpty()
                )
            ),
            hourlyForecast = dto.hourly?.map { hourly ->
                WeatherInfo(
                    dateTime = parseTimestampSecondsToLocalDateTime(hourly.dt ?: 0),
                    weatherCondition = WeatherCondition(
                        hourly.weather?.firstOrNull()?.id ?: 0,
                        hourly.weather?.firstOrNull()?.main.orEmpty(),
                        hourly.weather?.firstOrNull()?.description.orEmpty(),
                        hourly.weather?.firstOrNull()?.icon.orEmpty()
                    ),
                    precipitationProb = hourly.pop ?: 0.0,
                    temperature = hourly.temp ?: 0.0
                )
            } ?: listOf(),
            dailyForecast = dto.daily?.map { daily ->
                WeatherInfo(
                    dateTime = parseTimestampSecondsToLocalDateTime(daily.dt ?: 0),
                    weatherCondition = WeatherCondition(
                        daily.weather?.firstOrNull()?.id ?: 0,
                        daily.weather?.firstOrNull()?.main.orEmpty(),
                        daily.weather?.firstOrNull()?.description.orEmpty(),
                        daily.weather?.firstOrNull()?.icon.orEmpty()
                    ),
                    precipitationProb = daily.pop ?: 0.0,
                    temperature = daily.temp?.day ?: 0.0
                )
            } ?: listOf()
        )
    }

    fun mapToCity(dto: CityDto): City {
        return City(
            name = dto.name.orEmpty(),
            lat = dto.lat ?: 0.0,
            lon = dto.lon ?: 0.0
        )
    }
}