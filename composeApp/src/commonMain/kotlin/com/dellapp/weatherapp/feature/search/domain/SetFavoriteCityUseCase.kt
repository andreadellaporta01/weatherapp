package com.dellapp.weatherapp.feature.search.domain

import com.dellapp.weatherapp.core.domain.repository.AppDataStoreRepository
import com.dellapp.weatherapp.core.data.local.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.City
import kotlinx.serialization.json.Json

class SetFavoriteCityUseCase(
    private val appDataStoreRepository: AppDataStoreRepository
) {
    suspend operator fun invoke(city: City): Result<Unit> {
        try {
            val citiesJson = appDataStoreRepository.readValue(DataStoreKeys.FAVORITE_CITIES)
            var cities = mutableListOf<City>()
            if(!citiesJson.isNullOrEmpty()) {
                cities = Json.decodeFromString<MutableList<City>>(citiesJson)
            }
            if(!cities.contains(city)) {
                cities.add(city)
                appDataStoreRepository.setValue(DataStoreKeys.FAVORITE_CITIES, Json.encodeToString(cities))
            }
            appDataStoreRepository.setValue(DataStoreKeys.LAST_FAVORITE_CITY, Json.encodeToString(city))
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}