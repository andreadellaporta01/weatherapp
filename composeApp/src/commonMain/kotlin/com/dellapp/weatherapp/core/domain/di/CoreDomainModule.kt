package com.dellapp.weatherapp.core.domain.di

import com.dellapp.weatherapp.core.domain.usecase.GetLanguageUseCase
import com.dellapp.weatherapp.core.domain.usecase.SetLanguageUseCase
import org.koin.dsl.module

val coreDomainModule = module {
    factory { GetLanguageUseCase(get()) }
    factory { SetLanguageUseCase(get()) }
}