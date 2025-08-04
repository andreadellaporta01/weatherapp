package com.dellapp.weatherapp.core.data.repository

import com.dellapp.weatherapp.core.common.getByIsoCode
import com.dellapp.weatherapp.core.data.api.WeatherApiService
import com.dellapp.weatherapp.core.data.mapper.WeatherMapper
import com.dellapp.weatherapp.core.domain.model.Air
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.core.domain.model.Weather
import com.dellapp.weatherapp.core.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val apiService: WeatherApiService,
    private val mapper: WeatherMapper
) : WeatherRepository {

    override suspend fun getCityCoordinates(city: String): Result<List<City>> {
        return try {
            val dtoList = apiService.getCityCoordinates(city)
            val cities = dtoList.map { dto -> mapper.mapToCity(dto) }.toList()
            Result.success(cities)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeather(lat: Double, lon: Double, lang: String): Result<Weather> {
        return try {
            val weatherDto = apiService.getWeather(lat, lon, lang)
            val city = apiService.getCityByCoordinates(lat, lon).firstOrNull()
            val airQuality = apiService.getAirQuality(lat, lon)
            val cityName = city?.localNames?.getByIsoCode(lang) ?: city?.name
            val weather = mapper.mapToWeather(
                weatherDto,
                cityName,
                airQuality.quality?.firstOrNull()?.main?.aqi
            )
            Result.success(weather)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAirQuality(
        lat: Double,
        lon: Double
    ): Result<Air> {
        return try {
            val airDto = apiService.getAirQuality(lat, lon)
            Result.success(mapper.mapToAir(airDto))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}