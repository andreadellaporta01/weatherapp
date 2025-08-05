package com.dellapp.weatherapp.feature.settings.domain

import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.DataStoreKeys

class SetThemeUseCase(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke(theme: String): Result<Unit> {
        try {
            appDataStore.setValue(DataStoreKeys.THEME_STYLE, theme)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}