package com.dellapp.weatherapp.feature.home.ui

import app.cash.turbine.test
import com.dellapp.weatherapp.core.domain.model.Air
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.core.domain.model.CurrentWeather
import com.dellapp.weatherapp.core.domain.model.Weather
import com.dellapp.weatherapp.core.domain.model.WeatherCondition
import com.dellapp.weatherapp.core.domain.repository.AppDataStoreRepository
import com.dellapp.weatherapp.core.domain.repository.WeatherRepository
import com.dellapp.weatherapp.core.domain.usecase.GetWeatherByLocationUseCase
import com.dellapp.weatherapp.feature.home.domain.GetLastFavoriteCityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// --- FAKES ---
class FakeWeatherRepository : WeatherRepository {
    override suspend fun getCityCoordinates(city: String) = Result.success(listOf(City(name = "TestCity", lat = 0.0, lon = 0.0)))
    override suspend fun getWeather(lat: Double, lon: Double, lang: String) = Result.success(
        Weather(
            cityName = "TestCity",
            currentWeather = CurrentWeather(
                temperature = 25.0,
                minTemperature = 15.0,
                maxTemperature = 30.0,
                weatherCondition = WeatherCondition(1, "Clouds", "broken clouds", "04d"),
                uvi = 5.0,
                sunrise = LocalDateTime(2021, 1, 1, 6, 0),
                sunset = LocalDateTime(2021, 1, 1, 18, 0),
                windSpeed = 10.0,
                windAngle = 180,
                rain = 0.0,
                feelsLike = 24.0,
                humidity = 60,
                visibility = 10000,
                pressure = 1012,
                dewPoint = 10.0
            ),
            hourlyForecast = emptyList(),
            dailyForecast = emptyList(),
            airQuality = 1
        )
    )
    override suspend fun getAirQuality(lat: Double, lon: Double) = Result.success(Air(1))
}

class FakeAppDataStoreRepository : AppDataStoreRepository {
    override suspend fun setValue(key: String, value: String) {}
    override suspend fun readValue(key: String): String {
        val testCity = City(name = "TestCity", lat = 0.0, lon = 0.0)
        return Json.encodeToString(testCity)
    }
}

open class FakeGetWeatherByLocationUseCase : GetWeatherByLocationUseCase(FakeWeatherRepository(), FakeAppDataStoreRepository())
open class FakeGetLastFavoriteCityUseCase : GetLastFavoriteCityUseCase(FakeAppDataStoreRepository())

// --- TEST CLASS ---
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: HomeViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(FakeGetWeatherByLocationUseCase(), FakeGetLastFavoriteCityUseCase())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getPreferredCityHandlesSuccess() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("TestCity", state.weather?.cityName)
        }
    }

    @Test
    fun getCurrentWeatherByLocationHandlesSuccess() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(25.0, state.weather?.currentWeather?.temperature)
        }
    }
}


