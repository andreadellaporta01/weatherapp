package com.dellapp.weatherapp.feature.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.feature.search.domain.SearchCitiesUseCase
import com.dellapp.weatherapp.feature.search.domain.SetPreferredCityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val setPreferredCityUseCase: SetPreferredCityUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState(isLoading = true))
    val uiState: StateFlow<SearchUiState> = _uiState

    fun searchCity(query: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        searchCitiesUseCase(query)
            .onSuccess { cities ->
                _uiState.update { it.copy(cities = cities) }
            }
            .onFailure {
                _uiState.update { it.copy(cities = emptyList()) }
            }
        _uiState.update { it.copy(isLoading = false) }
    }

    fun setPreferredCity(city: City) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        setPreferredCityUseCase(city)
            .onSuccess {
                _uiState.update { it.copy(isPreferredCitySet = true) }
            }
            .onFailure {
                _uiState.update { it.copy(isPreferredCitySet = false) }
            }
        _uiState.update { it.copy(isLoading = false) }
    }
}