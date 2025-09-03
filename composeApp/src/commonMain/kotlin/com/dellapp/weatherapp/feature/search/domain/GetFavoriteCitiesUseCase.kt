package com.dellapp.weatherapp.feature.search.domain

import com.dellapp.weatherapp.core.domain.repository.AppDataStoreRepository
import com.dellapp.weatherapp.core.data.local.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.City
import kotlinx.serialization.json.Json

class GetFavoriteCitiesUseCase(
    private val appDataStoreRepository: AppDataStoreRepository
) {
    suspend operator fun invoke(): Result<List<City>> {
        try {
            val citiesJson = appDataStoreRepository.readValue(DataStoreKeys.FAVORITE_CITIES)
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