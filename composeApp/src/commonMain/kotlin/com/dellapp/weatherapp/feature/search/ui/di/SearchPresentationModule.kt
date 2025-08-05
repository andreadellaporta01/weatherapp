package com.dellapp.weatherapp.feature.search.ui.di

import com.dellapp.weatherapp.feature.search.ui.SearchViewModel
import com.dellapp.weatherapp.feature.search.ui.favorite.FavoriteCityViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchPresentationModule = module {
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { FavoriteCityViewModel(get()) }
}