package com.dellapp.weatherapp.feature.search.domain.di

import com.dellapp.weatherapp.feature.search.domain.SearchCitiesUseCase
import com.dellapp.weatherapp.feature.search.domain.SetPreferredCityUseCase
import org.koin.dsl.module

val searchDomainModule = module {
    factory { SearchCitiesUseCase(get()) }
    factory { SetPreferredCityUseCase(get()) }
}