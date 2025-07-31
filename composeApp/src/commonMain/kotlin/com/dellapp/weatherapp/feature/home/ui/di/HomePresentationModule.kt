package com.dellapp.weatherapp.feature.home.ui.di

import com.dellapp.weatherapp.feature.home.ui.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homePresentationModule = module {
    viewModel { HomeViewModel(get(), get()) }
}