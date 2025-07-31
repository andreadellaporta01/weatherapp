package com.dellapp.weatherapp.feature.home.domain

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.City
import kotlinx.serialization.json.Json

class GetPreferredCityUseCase(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(): Result<City> {
        try {
            val cityJson = appDataStore.readValue(DataStoreKeys.CITY)
            val city = Json.decodeFromString<City>(cityJson.orEmpty())
            return Result.success(city)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}