package com.dellapp.weatherapp.feature.settings.ui.di

import com.dellapp.weatherapp.feature.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModel { SettingsViewModel(get(), get()) }
}