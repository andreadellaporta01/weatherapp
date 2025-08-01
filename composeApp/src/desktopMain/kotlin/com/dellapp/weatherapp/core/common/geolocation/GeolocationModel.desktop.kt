package com.dellapp.weatherapp.core.common.geolocation

import dev.jordond.compass.geolocation.Locator
import dev.jordond.compass.geolocation.NotSupportedLocator

actual fun getPlatformLocator(): Locator {
    return NotSupportedLocator
}

actual val canShowAppSettings: Boolean = false
actual fun showAppSettings() {
}