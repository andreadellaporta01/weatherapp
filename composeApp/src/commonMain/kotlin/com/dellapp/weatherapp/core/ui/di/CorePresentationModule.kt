package com.dellapp.weatherapp.core.ui.di

import com.dellapp.weatherapp.core.ui.CoreViewModel
import com.dellapp.weatherapp.feature.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val corePresentationModule = module {
    single { CoreViewModel(get(), get()) }
}