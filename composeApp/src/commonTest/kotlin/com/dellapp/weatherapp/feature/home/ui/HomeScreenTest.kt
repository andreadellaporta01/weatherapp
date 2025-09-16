package com.dellapp.weatherapp.feature.home.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import com.dellapp.weatherapp.core.common.geolocation.GeolocationModel
import com.dellapp.weatherapp.core.common.geolocation.createGeolocator
import com.dellapp.weatherapp.core.domain.usecase.GetLanguageUseCase
import com.dellapp.weatherapp.core.domain.usecase.GetThemeUseCase
import com.dellapp.weatherapp.core.domain.usecase.GetWeatherByLocationUseCase
import com.dellapp.weatherapp.core.ui.CoreViewModel
import com.dellapp.weatherapp.feature.home.domain.GetLastFavoriteCityUseCase
import com.dellapp.weatherapp.feature.splash.ui.SplashScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.Test

// --- Fakes for CoreViewModel ---
open class FakeGetLanguageUseCase : GetLanguageUseCase(FakeAppDataStoreRepository())
open class FakeGetThemeUseCase : GetThemeUseCase(FakeAppDataStoreRepository())

class FakeGeolocatorModel : GeolocationModel(geolocator = createGeolocator())

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenCommonTest : KoinTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun showsWeatherWhenAvailable() = runComposeUiTest {
        stopKoin()
        startKoin {
            modules(
                module {
                    single<GetLastFavoriteCityUseCase> { FakeGetLastFavoriteCityUseCase() }
                    single<GetWeatherByLocationUseCase> { FakeGetWeatherByLocationUseCase() }
                    single<GetLanguageUseCase> { FakeGetLanguageUseCase() }
                    single<GetThemeUseCase> { FakeGetThemeUseCase() }
                    viewModel { HomeViewModel(get(), get()) }
                    viewModel { CoreViewModel(get(), get()) }
                }
            )
        }

        setContent {
            MaterialTheme {
                Navigator(screen = SplashScreen()) {
                    HomeScreen(FakeGeolocatorModel()).Content()
                }
            }
        }.apply {
            onNodeWithText("TestCity").assertIsDisplayed()
            onNodeWithText("broken clouds").assertIsDisplayed()
            onNodeWithText("25°").assertIsDisplayed()
        }
    }
}
