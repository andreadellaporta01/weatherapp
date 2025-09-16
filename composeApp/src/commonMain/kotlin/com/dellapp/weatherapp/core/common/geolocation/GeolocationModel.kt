package com.dellapp.weatherapp.core.common.geolocation

import cafe.adriel.voyager.core.model.screenModelScope
import dev.jordond.compass.Location
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.Locator
import dev.jordond.compass.geolocation.TrackingStatus
import dev.stateholder.extensions.voyager.StateScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

open class GeolocationModel(private val geolocator: Geolocator) : StateScreenModel<GeolocationModel.State>(State()) {

    init {
        screenModelScope.launch {
            val isAvailable = geolocator.isAvailable()
            updateState { it.copy(locationServiceAvailable = isAvailable) }
        }

        geolocator.trackingStatus
            .onEach { status ->
                updateState { it.copy(trackingLocation = status) }
            }
            .launchIn(screenModelScope)
    }

    open fun currentLocation() {
        screenModelScope.launch {
            updateState { it.copy(loading = true, lastResult = null, location = null) }
            geolocator.current().onSuccess { location ->
                updateState { it.copy(location = location, loading = false) }
            }.onFailed { geolocatorResultError ->
                updateState { it.copy(loading = false, lastResult = geolocatorResultError) }
            }
        }
    }

    data class State(
        val handlePermissions: Boolean = true,
        val loading: Boolean = false,
        val location: Location? = null,
        val lastResult: GeolocatorResult? = null,
        val locationServiceAvailable: Boolean = false,
        val trackingLocation: TrackingStatus = TrackingStatus.Idle,
        val lastLocation: GeolocatorResult? = null,
    ) {

        val tracking = trackingLocation.isActive
        val trackingError = (trackingLocation as? TrackingStatus.Error)?.cause
        val busy: Boolean = loading || tracking
        val permissionsDeniedForever: Boolean =
            lastResult is GeolocatorResult.PermissionDenied && lastResult.forever
    }

    open val permissionsDeniedForever: MutableStateFlow<Boolean> = MutableStateFlow(false)
    open val location: MutableStateFlow<Nothing?> = MutableStateFlow(null)
}

fun createGeolocator(): Geolocator {
    return Geolocator(getPlatformLocator())
}

expect fun getPlatformLocator(): Locator

expect val canShowAppSettings: Boolean

expect fun showAppSettings()