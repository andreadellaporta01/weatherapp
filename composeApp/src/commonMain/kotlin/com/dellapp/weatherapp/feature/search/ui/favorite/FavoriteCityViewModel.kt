package com.dellapp.weatherapp.feature.search.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.core.domain.usecase.GetWeatherByLocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteCityViewModel(
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoriteCityUiState(isLoading = true))
    val uiState: StateFlow<FavoriteCityUiState> = _uiState

    fun getCurrentWeather(city: City) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        getWeatherByLocationUseCase.invoke(city.lat, city.lon)
            .onSuccess { weather ->
                _uiState.update { it.copy(weather = weather, isLoading = false) }
            }
            .onFailure { exception ->
                _uiState.update { it.copy(error = exception.message, isLoading = false) }
            }
    }
}