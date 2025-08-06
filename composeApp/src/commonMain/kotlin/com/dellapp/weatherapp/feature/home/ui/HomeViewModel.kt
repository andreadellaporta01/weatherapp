package com.dellapp.weatherapp.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.core.domain.usecase.GetWeatherByLocationUseCase
import com.dellapp.weatherapp.feature.home.domain.GetLastFavoriteCityUseCase
import dev.jordond.compass.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val getLastFavoriteCityUseCase: GetLastFavoriteCityUseCase,
) : ViewModel() {

    private var lastLocation: Location? = null
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        getPreferredCity()
    }

    fun getPreferredCity() = viewModelScope.launch {
        getLastFavoriteCityUseCase.invoke()
            .onSuccess { city ->
                if(city != null) {
                    getCurrentWeather(city)
                } else {
                    _uiState.update { it.copy(weather = null, isLoading = false) }
                }
            }
            .onFailure {
                _uiState.update { it.copy(weather = null, isLoading = false) }
            }
    }

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

    fun getCurrentWeather(location: Location) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        getWeatherByLocationUseCase.invoke(
            location.coordinates.latitude,
            location.coordinates.longitude
        )
            .onSuccess { weather ->
                _uiState.update { it.copy(weather = weather, isLoading = false) }
            }
            .onFailure { exception ->
                _uiState.update { it.copy(error = exception.message, isLoading = false) }
            }
    }

    fun onLocationUpdate(location: Location?) {
        if (lastLocation != location) {
            lastLocation = location
            location?.let {
                getCurrentWeather(it)
            }
        }
    }
}