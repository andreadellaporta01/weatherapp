package com.dellapp.weatherapp.feature.settings.domain.di

import com.dellapp.weatherapp.core.domain.usecase.GetLanguageUseCase
import com.dellapp.weatherapp.core.domain.usecase.GetThemeUseCase
import com.dellapp.weatherapp.core.domain.usecase.GetWeatherByLocationUseCase
import com.dellapp.weatherapp.core.domain.usecase.SetLanguageUseCase
import com.dellapp.weatherapp.feature.settings.domain.SetThemeUseCase
import org.koin.dsl.module

val settingsDomainModule = module {
    factory { SetThemeUseCase(get()) }
}