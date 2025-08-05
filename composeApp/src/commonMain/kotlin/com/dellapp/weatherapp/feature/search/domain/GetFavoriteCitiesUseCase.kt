package com.dellapp.weatherapp.feature.search.domain

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.City
import kotlinx.serialization.json.Json

class GetFavoriteCitiesUseCase(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(): Result<List<City>> {
        try {
            val citiesJson = appDataStore.readValue(DataStoreKeys.FAVORITE_CITIES)
            var cities = mutableListOf<City>()
            if(!citiesJson.isNullOrEmpty()) {
                cities = Json.decodeFromString<MutableList<City>>(citiesJson)
            }
            return Result.success(cities)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}