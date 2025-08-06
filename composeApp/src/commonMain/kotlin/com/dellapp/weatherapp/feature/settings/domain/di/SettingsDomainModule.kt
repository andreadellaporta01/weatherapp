package com.dellapp.weatherapp.feature.settings.domain.di

import com.dellapp.weatherapp.feature.settings.domain.SetThemeUseCase
import org.koin.dsl.module

val settingsDomainModule = module {
    factory { SetThemeUseCase(get()) }
}