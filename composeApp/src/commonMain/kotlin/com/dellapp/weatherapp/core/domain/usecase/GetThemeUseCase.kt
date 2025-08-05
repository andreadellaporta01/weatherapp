package com.dellapp.weatherapp.core.domain.usecase

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys

class GetThemeUseCase(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(): Result<String?> {
        try {
            val theme = appDataStore.readValue(DataStoreKeys.THEME_STYLE)
            return Result.success(theme)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}