package com.dellapp.weatherapp.feature.search.domain

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys
import com.dellapp.weatherapp.core.domain.model.City
import kotlinx.serialization.json.Json

class SetPreferredCityUseCase(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(city: City): Result<Unit> {
        try {
            appDataStore.setValue(DataStoreKeys.CITY, Json.encodeToString(city))
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}