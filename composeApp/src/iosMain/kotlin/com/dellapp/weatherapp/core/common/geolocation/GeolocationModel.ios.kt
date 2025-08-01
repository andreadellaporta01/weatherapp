package com.dellapp.weatherapp.core.common.geolocation

import dev.jordond.compass.geolocation.Locator
import dev.jordond.compass.geolocation.mobile.mobile
import dev.jordond.compass.permissions.LocationPermissionController
import dev.jordond.compass.permissions.mobile.openSettings

actual fun getPlatformLocator(): Locator {
    return Locator.mobile()
}

actual val canShowAppSettings: Boolean = true
actual fun showAppSettings() {
    LocationPermissionController.openSettings()
}