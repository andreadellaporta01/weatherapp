package com.dellapp.weatherapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    initKoin(context = null) {
        modules()
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "WeatherApp",
        ) {
            App()
        }
    }
}