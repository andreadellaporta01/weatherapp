package com.dellapp.weatherapp.core.data.repository

import CityDto
import com.dellapp.weatherapp.core.data.api.WeatherApiService
import com.dellapp.weatherapp.core.data.dto.AirDto
import com.dellapp.weatherapp.core.data.dto.WeatherDto
import com.dellapp.weatherapp.core.data.mapper.WeatherMapper
import com.dellapp.weatherapp.core.domain.model.Air
import com.dellapp.weatherapp.core.domain.model.City
import com.dellapp.weatherapp.core.domain.model.CurrentWeather
import com.dellapp.weatherapp.core.domain.model.Weather
import com.dellapp.weatherapp.core.domain.model.WeatherCondition
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.*

class FakeApiService : WeatherApiService() {
    var shouldFail = false
    override suspend fun getCityCoordinates(city: String): List<CityDto> {
        if (shouldFail) throw Exception("Failed city")
        return listOf(
            CityDto(
                name = "TestCity",
                lat = 1.0,
                lon = 2.0,
                country = "TC",
                localNames = null
            )
        )
    }

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): WeatherDto {
        if (shouldFail) throw Exception("Failed weather")
        return WeatherDto(current = null, daily = null, hourly = null)
    }

    override suspend fun getAirQuality(lat: Double, lon: Double): AirDto {
        if (shouldFail) throw Exception("Failed air")
        return AirDto(quality = listOf(/*...*/))
    }
    // Implement other necessary methods if needed
}

class FakeMapper : WeatherMapper() {
    override fun mapToCity(dto: CityDto): City = City("TestCity", "IT", 2.0, 2.0)
    override fun mapToWeather(dto: WeatherDto, cityName: String?, airQuality: Int?): Weather {
        // Mock implementation for mapToWeather
        return Weather(
            cityName ?: "TestCity",
            currentWeather = CurrentWeather(
                temperature = 20.0,
                minTemperature = 15.0,
                maxTemperature = 25.0,
                weatherCondition = WeatherCondition(
                    id = 1,
                    main = "Clouds",
                    description = "broken clouds",
                    iconCode = "04d",
                ),
                uvi = 5.0,
                sunrise = LocalDateTime(2021, 1, 1, 6, 0),
                sunset = LocalDateTime(2021, 1, 1, 6, 0),
                windSpeed = 10.0,
                windAngle = 180,
                rain = 0.0,
                feelsLike = 19.0,
                humidity = 60,
                visibility = 10000,
                pressure = 1012,
                dewPoint = 10.0
            ),
            hourlyForecast = emptyList(),
            dailyForecast = emptyList(),
            airQuality = airQuality ?: 1
        )
    }

    override fun mapToAir(dto: AirDto): Air = Air(airQuality = 1)
}

class WeatherRepositoryImplTest {
    private lateinit var apiService: FakeApiService
    private lateinit var mapper: FakeMapper
    private lateinit var repository: WeatherRepositoryImpl

    @BeforeTest
    fun setup() {
        apiService = FakeApiService()
        mapper = FakeMapper()
        repository = WeatherRepositoryImpl(apiService, mapper)
    }

    @Test
    fun getCityCoordinates_returnsCityList_onSuccess() = runTest {
        val result = repository.getCityCoordinates("TestCity")
        assertTrue(result.isSuccess)
        assertEquals("TestCity", result.getOrNull()?.first()?.name)
    }

    @Test
    fun getCityCoordinates_returnsFailure_onException() = runTest {
        apiService.shouldFail = true
        val result = repository.getCityCoordinates("TestCity")
        assertTrue(result.isFailure)
    }
}