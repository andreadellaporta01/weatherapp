package com.dellapp.weatherapp.feature.search.ui

import com.dellapp.weatherapp.core.domain.model.City

data class SearchUiState(
    val isLoading: Boolean = false,
    val isPreferredCitySet: Boolean = false,
    val cities: List<City>? = null,
)