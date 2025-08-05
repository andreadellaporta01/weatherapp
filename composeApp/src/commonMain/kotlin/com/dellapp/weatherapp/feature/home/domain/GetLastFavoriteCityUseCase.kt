package com.dellapp.weatherapp.feature.home.domain

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.City
import kotlinx.serialization.json.Json

class GetLastFavoriteCityUseCase(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(): Result<City?> {
        try {
            val cityJson = appDataStore.readValue(DataStoreKeys.LAST_FAVORITE_CITY)
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