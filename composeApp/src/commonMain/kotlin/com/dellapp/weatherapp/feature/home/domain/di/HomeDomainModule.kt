package com.dellapp.weatherapp.feature.home.domain.di

import com.dellapp.weatherapp.feature.home.domain.GetLastFavoriteCityUseCase
import org.koin.dsl.module

val homeDomainModule = module {
    factory { GetLastFavoriteCityUseCase(get()) }
}