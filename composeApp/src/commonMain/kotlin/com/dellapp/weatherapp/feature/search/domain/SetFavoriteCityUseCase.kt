package com.dellapp.weatherapp.feature.search.domain

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.City
import kotlinx.serialization.json.Json

class SetFavoriteCityUseCase(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(city: City): Result<Unit> {
        try {
            val citiesJson = appDataStore.readValue(DataStoreKeys.FAVORITE_CITIES)
            var cities = mutableListOf<City>()
            if(!citiesJson.isNullOrEmpty()) {
                cities = Json.decodeFromString<MutableList<City>>(citiesJson)
            }
            if(!cities.contains(city)) {
                cities.add(city)
                appDataStore.setValue(DataStoreKeys.FAVORITE_CITIES, Json.encodeToString(cities))
            }
            appDataStore.setValue(DataStoreKeys.LAST_FAVORITE_CITY, Json.encodeToString(city))
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}