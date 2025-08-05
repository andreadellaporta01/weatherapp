package com.dellapp.weatherapp.feature.search.ui

import com.dellapp.weatherapp.core.domain.model.City

data class SearchUiState(
    val isSuggestionLoading: Boolean = false,
    val isPreferredCitiesLoading: Boolean = false,
    val cities: List<City>? = null,
    val preferredCities: List<City>? = null,
)