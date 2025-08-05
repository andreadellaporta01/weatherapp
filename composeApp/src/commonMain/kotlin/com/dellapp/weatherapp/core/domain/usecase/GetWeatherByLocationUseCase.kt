package com.dellapp.weatherapp.core.domain.usecase

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.Weather
import com.dellapp.weatherapp.core.domain.repository.WeatherRepository

class GetWeatherByLocationUseCase(
    private val repository: WeatherRepository,
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<Weather> {
        try {
            val lang = appDataStore.readValue(DataStoreKeys.LANGUAGE)
            return repository.getWeather(lat, lon, lang ?: "en")
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}