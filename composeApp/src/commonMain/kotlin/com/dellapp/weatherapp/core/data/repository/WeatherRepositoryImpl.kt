package com.dellapp.weatherapp.core.data.repository

import com.dellapp.weatherapp.core.data.api.WeatherApiService
import com.dellapp.weatherapp.core.data.mapper.WeatherMapper
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
            val dto = apiService.getWeather(lat, lon, lang)
            val weather = mapper.mapToWeather(dto)
            Result.success(weather)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}