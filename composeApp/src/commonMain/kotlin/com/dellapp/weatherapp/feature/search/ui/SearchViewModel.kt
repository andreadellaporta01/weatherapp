package com.dellapp.weatherapp.feature.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.feature.search.domain.DeleteFavoriteCityUseCase
import com.dellapp.weatherapp.feature.search.domain.GetFavoriteCitiesUseCase
import com.dellapp.weatherapp.feature.search.domain.SearchCitiesUseCase
import com.dellapp.weatherapp.feature.search.domain.SetFavoriteCityUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val setFavoriteCityUseCase: SetFavoriteCityUseCase,
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val deleteFavoriteCityUseCase: DeleteFavoriteCityUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _events = MutableSharedFlow<SearchEvent>()
    val events = _events.asSharedFlow()

    init {
        getPreferredCities()
    }

    fun searchCity(query: String) = viewModelScope.launch {
        _uiState.update { it.copy(isSuggestionLoading = true) }
        searchCitiesUseCase(query)
            .onSuccess { cities ->
                _uiState.update { it.copy(cities = cities, isSuggestionLoading = false) }
            }
            .onFailure {
                _uiState.update { it.copy(cities = emptyList(), isSuggestionLoading = false) }
            }
    }

    fun setPreferredCity(city: City) = viewModelScope.launch {
        _uiState.update { it.copy(isSuggestionLoading = true, isPreferredCitiesLoading = true) }
        setFavoriteCityUseCase(city)
            .onSuccess {
                _events.emit(SearchEvent.PreferredCitySelected)
                _uiState.update {
                    it.copy(
                        isSuggestionLoading = false,
                        isPreferredCitiesLoading = false
                    )
                }
            }
            .onFailure {
                _uiState.update {
                    it.copy(
                        isSuggestionLoading = false,
                        isPreferredCitiesLoading = false
                    )
                }
            }
    }

    fun getPreferredCities() = viewModelScope.launch {
        _uiState.update { it.copy(isPreferredCitiesLoading = true) }
        getFavoriteCitiesUseCase()
            .onSuccess { cities ->
                _uiState.update {
                    it.copy(
                        preferredCities = cities,
                        isPreferredCitiesLoading = false
                    )
                }
            }
            .onFailure {
                _uiState.update {
                    it.copy(
                        preferredCities = listOf(),
                        isPreferredCitiesLoading = false
                    )
                }
            }
    }

    fun deletePreferredCity(city: City) = viewModelScope.launch {
        _uiState.update { it.copy(isPreferredCitiesLoading = true) }
        deleteFavoriteCityUseCase(city)
            .onSuccess { cities ->
                _uiState.update {
                    it.copy(
                        preferredCities = cities,
                        isPreferredCitiesLoading = false
                    )
                }
            }
            .onFailure {
                _uiState.update {
                    it.copy(
                        preferredCities = listOf(),
                        isPreferredCitiesLoading = false
                    )
                }
            }
    }

    sealed class SearchEvent {
        object PreferredCitySelected : SearchEvent()
    }
}