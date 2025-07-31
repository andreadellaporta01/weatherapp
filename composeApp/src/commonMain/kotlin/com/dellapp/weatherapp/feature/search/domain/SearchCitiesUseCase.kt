package com.dellapp.weatherapp.feature.search.domain

import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.core.domain.repository.WeatherRepository

class SearchCitiesUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Result<List<City>> {
        return repository.getCityCoordinates(city)
    }
}