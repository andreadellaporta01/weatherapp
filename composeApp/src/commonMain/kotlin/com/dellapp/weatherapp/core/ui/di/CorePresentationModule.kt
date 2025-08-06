package com.dellapp.weatherapp.core.ui.di

import com.dellapp.weatherapp.core.ui.CoreViewModel
import org.koin.dsl.module

val corePresentationModule = module {
    single { CoreViewModel(get(), get()) }
}