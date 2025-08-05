package com.dellapp.weatherapp.feature.search.ui.favorite

import com.dellapp.weatherapp.core.domain.model.Weather

data class FavoriteCityUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val weather: Weather? = null,
)