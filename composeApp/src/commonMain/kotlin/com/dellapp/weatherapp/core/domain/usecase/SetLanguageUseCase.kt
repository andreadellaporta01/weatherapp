package com.dellapp.weatherapp.core.domain.usecase

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys

class SetLanguageUseCase(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(language: String): Result<Unit> {
        try {
            appDataStore.setValue(DataStoreKeys.LANGUAGE, language)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}