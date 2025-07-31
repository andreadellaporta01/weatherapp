package com.dellapp.weatherapp.core.data.di

import com.dellapp.weatherapp.core.common.AppDataStoreManager
import com.dellapp.weatherapp.core.data.api.WeatherApiService
import com.dellapp.weatherapp.core.data.mapper.WeatherMapper
import com.dellapp.weatherapp.core.data.repository.WeatherRepositoryImpl
import com.dellapp.weatherapp.core.domain.repository.WeatherRepository
import org.koin.dsl.module

val coreDataModule = module {
    factory<WeatherApiService> { WeatherApiService() }
    factory<WeatherMapper> { WeatherMapper() }
    factory<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
}