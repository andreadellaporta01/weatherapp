package com.dellapp.weatherapp.core.data.api

import CityDto
import com.dellapp.weatherapp.core.data.dto.WeatherDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class WeatherApiService {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
        }
    }

    private val baseUrl = "https://api.openweathermap.org/data/3.0"
    private val baseUrlGeo = "https://api.openweathermap.org/geo/1.0"
    private val apiKey = "0c49d3c482fdd4b539ee199514fef912"

    suspend fun getCityCoordinates(
        city: String,
    ): List<CityDto> {
        return httpClient.get("$baseUrlGeo/direct") {
            parameter("q", city)
            parameter("limit", 5)
            parameter("appid", apiKey)
        }.body()
    }

    suspend fun getCityByCoordinates(
        lat: Double,
        lon: Double,
    ): List<CityDto> {
        return httpClient.get("$baseUrlGeo/reverse") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("limit", 1)
            parameter("appid", apiKey)
        }.body()
    }

    suspend fun getWeather(
        lat: Double, lon: Double, lang: String, units: String = "metric"
    ): WeatherDto {
        return httpClient.get("$baseUrl/onecall") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("appid", apiKey)
            parameter("units", units)
            parameter("lang", lang)
            parameter("exclude", "minutely,alerts")
        }.body()
    }

    fun close() {
        httpClient.close()
    }
}