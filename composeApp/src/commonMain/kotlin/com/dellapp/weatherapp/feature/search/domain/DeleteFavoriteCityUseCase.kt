package com.dellapp.weatherapp.feature.search.domain

import com.dellapp.weatherapp.core.data.local.AppDataStore
import com.dellapp.weatherapp.core.data.local.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.City
import kotlinx.serialization.json.Json

class DeleteFavoriteCityUseCase(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(city: City): Result<List<City>> {
        try {
            val citiesJson = appDataStore.readValue(DataStoreKeys.FAVORITE_CITIES)
            var cities = mutableListOf<City>()
            if(!citiesJson.isNullOrEmpty()) {
                cities = Json.decodeFromString<MutableList<City>>(citiesJson)
            }
            cities.remove(city)
            appDataStore.setValue(DataStoreKeys.FAVORITE_CITIES, Json.encodeToString(cities))
            return Result.success(cities)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}