package com.dellapp.weatherapp.core.common.geolocation

import dev.jordond.compass.geolocation.Locator
import dev.jordond.compass.geolocation.browser.browser

actual fun getPlatformLocator(): Locator {
    return Locator.browser()
}

actual val canShowAppSettings: Boolean = false
actual fun showAppSettings() {
}