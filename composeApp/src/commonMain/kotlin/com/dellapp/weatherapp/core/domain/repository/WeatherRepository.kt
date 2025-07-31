package com.dellapp.weatherapp.core.domain.repository

import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.core.domain.model.Weather

interface WeatherRepository {
    suspend fun getCityCoordinates(city: String): Result<List<City>>
    suspend fun getWeather(lat: Double, lon: Double, lang: String): Result<Weather>
}