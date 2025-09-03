package com.dellapp.weatherapp.feature.settings.domain

import com.dellapp.weatherapp.core.domain.repository.AppDataStoreRepository
import com.dellapp.weatherapp.core.data.local.DataStoreKeys

class SetThemeUseCase(
    private val appDataStoreRepository: AppDataStoreRepository
) {
    suspend operator fun invoke(theme: String): Result<Unit> {
        try {
            appDataStoreRepository.setValue(DataStoreKeys.THEME_STYLE, theme)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}