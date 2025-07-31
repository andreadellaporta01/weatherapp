package com.dellapp.weatherapp.core.domain.usecase

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys

class GetLanguageUseCase(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(): Result<String> {
        try {
            val language = appDataStore.readValue(DataStoreKeys.LANGUAGE)
            return Result.success(language ?: "en")
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}