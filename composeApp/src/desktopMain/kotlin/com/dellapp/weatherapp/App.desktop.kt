package com.dellapp.weatherapp

import com.dellapp.weatherapp.core.common.Localization
import org.koin.dsl.module

actual val targetModule = module {
    single<Localization> { Localization() }
}