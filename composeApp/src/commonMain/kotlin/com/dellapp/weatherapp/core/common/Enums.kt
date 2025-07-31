package com.dellapp.weatherapp.core.common

enum class ForecastType {
    HOURLY, WEEKLY
}

enum class Language(val iso: String) {
    English(iso = "en"),
    Italiano(iso = "it"),
    Español(iso = "es")
}