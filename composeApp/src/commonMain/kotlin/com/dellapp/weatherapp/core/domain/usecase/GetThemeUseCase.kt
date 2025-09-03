package com.dellapp.weatherapp.core.domain.usecase

import com.dellapp.weatherapp.core.domain.repository.AppDataStoreRepository
import com.dellapp.weatherapp.core.data.local.DataStoreKeys

class GetThemeUseCase(
    private val appDataStoreRepository: AppDataStoreRepository
) {
    suspend operator fun invoke(): Result<String?> {
        try {
            val theme = appDataStoreRepository.readValue(DataStoreKeys.THEME_STYLE)
            return Result.success(theme)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}