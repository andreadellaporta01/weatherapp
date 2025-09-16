package com.dellapp.weatherapp.feature.home.domain

import com.dellapp.weatherapp.core.domain.repository.AppDataStoreRepository
import com.dellapp.weatherapp.core.data.local.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.City
import kotlinx.serialization.json.Json

open class GetLastFavoriteCityUseCase(
    private val appDataStoreRepository: AppDataStoreRepository
) {
    open suspend operator fun invoke(): Result<City?> {
        try {
            val cityJson = appDataStoreRepository.readValue(DataStoreKeys.LAST_FAVORITE_CITY)
            var city: City? = null
            if (!cityJson.isNullOrEmpty()) {
                city = Json.decodeFromString<City>(cityJson)
            }
            return Result.success(city)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}