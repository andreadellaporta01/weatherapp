package com.dellapp.weatherapp.feature.home.domain.di

import com.dellapp.weatherapp.feature.home.domain.GetPreferredCityUseCase
import com.dellapp.weatherapp.feature.home.domain.GetWeatherByLocationUseCase
import org.koin.dsl.module

val homeDomainModule = module {
    factory { GetWeatherByLocationUseCase(get(), get()) }
    factory { GetPreferredCityUseCase(get()) }
}