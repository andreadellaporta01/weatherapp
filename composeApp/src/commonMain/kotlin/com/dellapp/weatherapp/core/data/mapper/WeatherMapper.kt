package com.dellapp.weatherapp.core.data.mapper

import CityDto
import com.dellapp.weatherapp.core.common.parseTimestampSecondsToLocalDateTime
import com.dellapp.weatherapp.core.data.dto.AirDto
import com.dellapp.weatherapp.core.data.dto.WeatherDto
import com.dellapp.weatherapp.core.domain.model.Air
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.core.domain.model.CurrentWeather
import com.dellapp.weatherapp.core.domain.model.Weather
import com.dellapp.weatherapp.core.domain.model.WeatherCondition
import com.dellapp.weatherapp.core.domain.model.WeatherInfo

class WeatherMapper {
    fun mapToWeather(dto: WeatherDto, cityName: String?, airQuality: Int?): Weather {
        return Weather(
            cityName = cityName.orEmpty(),
            airQuality = airQuality,
            currentWeather = CurrentWeather(
                temperature = dto.current?.temp ?: 0.0,
                minTemperature = dto.daily?.firstOrNull()?.temp?.min ?: 0.0,
                maxTemperature = dto.daily?.firstOrNull()?.temp?.max ?: 0.0,
                weatherCondition = WeatherCondition(
                    dto.current?.weather?.firstOrNull()?.id ?: 0,
                    dto.current?.weather?.firstOrNull()?.main.orEmpty(),
                    dto.current?.weather?.firstOrNull()?.description.orEmpty(),
                    dto.current?.weather?.firstOrNull()?.icon.orEmpty()
                ),
                uvi = dto.current?.uvi ?: 0.0,
                sunrise = parseTimestampSecondsToLocalDateTime(dto.current?.sunrise ?: 0),
                sunset = parseTimestampSecondsToLocalDateTime(dto.current?.sunset ?: 0),
                windSpeed = dto.current?.windSpeed ?: 0.0,
                windAngle = dto.current?.windDeg ?: 0,
                rain = dto.current?.rain?.lastHour ?: 0.0,
                feelsLike = dto.current?.feelsLike ?: 0.0,
                humidity = dto.current?.humidity ?: 0,
                visibility = (dto.current?.visibility?.div(1000)) ?: 0,
                pressure = dto.current?.pressure ?: 0,
                dewPoint = dto.daily?.firstOrNull()?.dewPoint ?: 0.0
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

    fun mapToAir(dto: AirDto): Air {
        return Air(
            airQuality = dto.quality?.firstOrNull()?.main?.aqi ?: 0
        )
    }
}