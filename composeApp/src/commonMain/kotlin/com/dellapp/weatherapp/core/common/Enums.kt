package com.dellapp.weatherapp.core.common

enum class ForecastType {
    HOURLY, WEEKLY
}

enum class Language(val iso: String) {
    English(iso = "en"),
    Italiano(iso = "it"),
    Español(iso = "es");

    companion object {
        fun fromIso(iso: String): Language =
            entries.firstOrNull { it.iso == iso } ?: English
    }
}

enum class ThemeStyle(val theme: String) {
    Light(theme = "light"),
    Dark(theme = "dark");


    companion object {
        fun fromTheme(theme: String): ThemeStyle =
            ThemeStyle.entries.firstOrNull { it.theme == theme } ?: Dark
    }
}

enum class Platform {
    Android,
    iOS,
    WASM,
    Desktop
}