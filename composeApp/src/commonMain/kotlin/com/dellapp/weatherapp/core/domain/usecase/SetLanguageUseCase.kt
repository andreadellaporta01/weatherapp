package com.dellapp.weatherapp.core.domain.usecase

import com.dellapp.weatherapp.core.domain.repository.AppDataStoreRepository
import com.dellapp.weatherapp.core.data.local.DataStoreKeys

class SetLanguageUseCase(
    private val appDataStoreRepository: AppDataStoreRepository
) {
    suspend operator fun invoke(language: String): Result<Unit> {
        try {
            appDataStoreRepository.setValue(DataStoreKeys.LANGUAGE, language)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}