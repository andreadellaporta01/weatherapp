package com.dellapp.weatherapp.feature.home.ui

import com.dellapp.weatherapp.core.domain.model.Weather

data class HomeUiState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String? = null,
)