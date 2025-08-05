package com.dellapp.weatherapp.feature.search.domain.di

import com.dellapp.weatherapp.feature.search.domain.DeleteFavoriteCityUseCase
import com.dellapp.weatherapp.feature.search.domain.GetFavoriteCitiesUseCase
import com.dellapp.weatherapp.feature.search.domain.SearchCitiesUseCase
import com.dellapp.weatherapp.feature.search.domain.SetFavoriteCityUseCase
import org.koin.dsl.module

val searchDomainModule = module {
    factory { SearchCitiesUseCase(get()) }
    factory { SetFavoriteCityUseCase(get()) }
    factory { GetFavoriteCitiesUseCase(get()) }
    factory { DeleteFavoriteCityUseCase(get()) }
}