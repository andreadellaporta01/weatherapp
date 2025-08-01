package com.dellapp.weatherapp.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dellapp.weatherapp.core.common.BottomBarHeight
import com.dellapp.weatherapp.core.common.Language
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.Shapes
import com.dellapp.weatherapp.core.common.TinySpacing
import com.dellapp.weatherapp.core.common.XXLargeSpacing
import com.dellapp.weatherapp.core.ui.components.BottomBar
import com.dellapp.weatherapp.core.ui.components.ForecastListCard
import com.dellapp.weatherapp.feature.search.ui.SearchScreen
import com.dellapp.weatherapp.feature.settings.ui.SettingsScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.add_city
import weatherapp.composeapp.generated.resources.data_not_found
import weatherapp.composeapp.generated.resources.main_background

class HomeScreen(
    private val selectedLanguage: Language,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel: HomeViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val uiState by viewModel.uiState.collectAsState()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.main_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier.padding(WindowInsets.safeContent.asPaddingValues()).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(XXLargeSpacing))
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else if (uiState.error != null) {
                    Text(
                        uiState.error ?: stringResource(Res.string.data_not_found),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge
                    )
                } else if (uiState.weather == null) {
                    Column(
                        modifier = Modifier.padding(all = LargeSpacing),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(Res.string.add_city),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                } else {
                    Text(
                        text = uiState.cityName.orEmpty(),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = uiState.weather?.currentWeather?.getTemperatureFormatted().orEmpty(),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = uiState.weather?.currentWeather?.weatherCondition?.description.orEmpty(),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(Modifier.height(TinySpacing))
                    Row {
                        Text(
                            text = "H: ${uiState.weather?.currentWeather?.getMaxFormatted()}",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 20.sp)
                        )
                        Spacer(modifier = Modifier.width(MediumSpacing))
                        Text(
                            text = "L: ${uiState.weather?.currentWeather?.getMinFormatted()}",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 20.sp)
                        )
                    }
                }
            }

            ForecastListCard(
                hourlyWeatherInfos = uiState.weather?.hourlyForecast.orEmpty(),
                weeklyWeatherInfos = uiState.weather?.dailyForecast.orEmpty(),
                isLoading = uiState.isLoading,
                modifier = Modifier.align(Alignment.BottomCenter).heightIn(min = 200.dp),
                paddingValues = PaddingValues(bottom = BottomBarHeight, top = 20.dp),
                language = selectedLanguage
            )

            BottomBar(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter), onAddClick = {
               navigator.push(SearchScreen {
                   viewModel.getPreferredCity()
               })
            }, onSettingsClick = {
                navigator.push(SettingsScreen(selectedLanguage))
            })
        }
    }
}