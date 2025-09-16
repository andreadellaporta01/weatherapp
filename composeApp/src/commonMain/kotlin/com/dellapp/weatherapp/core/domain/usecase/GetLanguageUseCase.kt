package com.dellapp.weatherapp.core.domain.usecase

import com.dellapp.weatherapp.core.domain.repository.AppDataStoreRepository
import com.dellapp.weatherapp.core.data.local.DataStoreKeys

open class GetLanguageUseCase(
    private val appDataStoreRepository: AppDataStoreRepository
) {
    suspend operator fun invoke(): Result<String> {
        try {
            val language = appDataStoreRepository.readValue(DataStoreKeys.LANGUAGE)
            return Result.success(language ?: "en")
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}