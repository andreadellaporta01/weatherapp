package com.dellapp.weatherapp.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val name: String,
    val country: String? = null,
    val lat: Double,
    val lon: Double,
)