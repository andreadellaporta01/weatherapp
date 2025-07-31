package com.dellapp.weatherapp.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.feature.home.domain.GetPreferredCityUseCase
import com.dellapp.weatherapp.feature.home.domain.GetWeatherByLocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val getPreferredCityUseCase: GetPreferredCityUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        getPreferredCity()
    }

    fun getPreferredCity() = viewModelScope.launch {
        getPreferredCityUseCase.invoke()
            .onSuccess { city ->
                _uiState.update { it.copy(cityName = city.name) }
                getCurrentWeather(city)
            }
            .onFailure {
                _uiState.update { it.copy(weather = null, isLoading = false) }
            }
    }

    fun getCurrentWeather(city: City) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        getWeatherByLocationUseCase.invoke(city.lat, city.lon)
            .onSuccess { weather ->
                _uiState.update { it.copy(weather = weather) }
            }
            .onFailure { exception ->
                _uiState.update { it.copy(error = exception.message) }
            }
        _uiState.update { it.copy(isLoading = false) }
    }
}