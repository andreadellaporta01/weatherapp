package com.dellapp.weatherapp.feature.home.domain

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.Weather
import com.dellapp.weatherapp.core.domain.repository.WeatherRepository

class GetWeatherByLocationUseCase(
    private val repository: WeatherRepository,
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<Weather> {
        if (lat !in -90.0..90.0 || lon !in -180.0..180.0) {
            return Result.failure(IllegalArgumentException("Not valid coordinates"))
        }

        try {
            val lang = appDataStore.readValue(DataStoreKeys.LANGUAGE)
            return repository.getWeather(lat, lon, lang ?: "en")
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}