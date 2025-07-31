package com.dellapp.weatherapp

import com.dellapp.weatherapp.core.common.Context
import com.dellapp.weatherapp.core.common.Localization
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val targetModule = module {
    single<Localization> { Localization(context = androidContext() as Context) }
}