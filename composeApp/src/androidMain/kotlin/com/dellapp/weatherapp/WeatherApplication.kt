package com.dellapp.weatherapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this) {
            androidContext(this@WeatherApplication)
            androidLogger()
            modules()
        }
    }
}