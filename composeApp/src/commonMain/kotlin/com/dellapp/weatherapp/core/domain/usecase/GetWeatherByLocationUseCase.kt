package com.dellapp.weatherapp.core.domain.usecase

import com.dellapp.weatherapp.core.domain.repository.AppDataStoreRepository
import com.dellapp.weatherapp.core.data.local.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.Weather
import com.dellapp.weatherapp.core.domain.repository.WeatherRepository

class GetWeatherByLocationUseCase(
    private val repository: WeatherRepository,
    private val appDataStoreRepository: AppDataStoreRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<Weather> {
        try {
            val lang = appDataStoreRepository.readValue(DataStoreKeys.LANGUAGE)
            return repository.getWeather(lat, lon, lang ?: "en")
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}