package com.dellapp.weatherapp.feature.home.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.Shapes
import com.dellapp.weatherapp.core.common.SmallSpacing
import com.dellapp.weatherapp.core.common.ThemeStyle
import com.dellapp.weatherapp.core.common.TinySpacing
import com.dellapp.weatherapp.core.common.XLargeSpacing
import com.dellapp.weatherapp.core.common.XXXLargeSpacing
import com.dellapp.weatherapp.core.common.geolocation.GeolocationModel
import com.dellapp.weatherapp.core.common.geolocation.canShowAppSettings
import com.dellapp.weatherapp.core.common.geolocation.showAppSettings
import com.dellapp.weatherapp.core.ui.CoreViewModel
import com.dellapp.weatherapp.core.ui.components.BottomBar
import com.dellapp.weatherapp.core.ui.components.ForecastListCard
import com.dellapp.weatherapp.core.ui.components.GradientBox
import com.dellapp.weatherapp.core.ui.components.GradientType
import com.dellapp.weatherapp.core.ui.components.WeatherDetail
import com.dellapp.weatherapp.feature.search.ui.SearchScreen
import com.dellapp.weatherapp.feature.settings.ui.SettingsScreen
import dev.stateholder.extensions.collectAsState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.add_city
import weatherapp.composeapp.generated.resources.data_not_found
import weatherapp.composeapp.generated.resources.location_permission_required
import weatherapp.composeapp.generated.resources.main_background
import weatherapp.composeapp.generated.resources.main_background_white
import weatherapp.composeapp.generated.resources.open_settings
import kotlin.math.roundToInt

class HomeScreen(private val model: GeolocationModel) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val coreViewModel: CoreViewModel = getKoin().get()
        val viewModel: HomeViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val uiState by viewModel.uiState.collectAsState()
        val selectedLanguage by coreViewModel.selectedLanguage.collectAsState()
        val selectedTheme by coreViewModel.selectedThemeStyle.collectAsState()
        val state by model.collectAsState()
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val permissionRequired = stringResource(Res.string.location_permission_required)
        val openSettings = stringResource(Res.string.open_settings)

        LaunchedEffect(state.location) {
            state.location?.let {
                viewModel.onLocationUpdate(it)
            }
        }

        if (canShowAppSettings) {
            LaunchedEffect(state.permissionsDeniedForever) {
                if (state.permissionsDeniedForever) {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = permissionRequired,
                            actionLabel = openSettings,
                            duration = SnackbarDuration.Long,
                        )

                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                showAppSettings()
                            }

                            else -> {}
                        }
                    }
                }
            }
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                val screenHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
                val fullHeightDp = maxHeight
                val peekHeight = with(LocalDensity.current) { 300.dp.toPx() }
                val initialOffset = screenHeightPx - peekHeight
                val offsetY = remember { Animatable(initialOffset) }
                val coroutineScope = rememberCoroutineScope()
                val isSystemDark = isSystemInDarkTheme()
                val themeStyle = selectedTheme
                    ?: (if (isSystemDark) ThemeStyle.Dark else ThemeStyle.Light)

                Image(
                    painter = painterResource(if (themeStyle == ThemeStyle.Dark) Res.drawable.main_background else Res.drawable.main_background_white),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                PullToRefreshBox(
                    isRefreshing = uiState.isLoading,
                    onRefresh = viewModel::getPreferredCity
                ) {
                    Column(
                        modifier = Modifier.padding(WindowInsets.safeContent.asPaddingValues())
                            .fillMaxWidth().verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(XXXLargeSpacing))
                        if (uiState.isLoading) {
                            CircularProgressIndicator()
                        } else if (uiState.error != null) {
                            Text(
                                uiState.error ?: stringResource(Res.string.data_not_found),
                                color = MaterialTheme.colorScheme.primary,
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
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge
                                )
                            }
                        } else {
                            Text(
                                text = uiState.weather?.cityName.orEmpty(),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                text = uiState.weather?.currentWeather?.getTemperatureFormatted()
                                    .orEmpty(),
                                color = MaterialTheme.colorScheme.primary,
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
                }

                GradientBox(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                        MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    gradientType = GradientType.LINEAR,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fullHeightDp)
                        .offset { IntOffset(0, offsetY.value.roundToInt()) }
                        .align(Alignment.TopStart)
                        .clip(Shapes.extraLarge)
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { change, dragAmount ->
                                    val newOffset =
                                        (offsetY.value + dragAmount).coerceIn(0f, initialOffset)
                                    coroutineScope.launch {
                                        offsetY.snapTo(newOffset)
                                    }
                                    change.consume()
                                },
                                onDragEnd = {
                                    coroutineScope.launch {
                                        val target = if (offsetY.value < initialOffset / 2) {
                                            0f
                                        } else {
                                            initialOffset
                                        }
                                        offsetY.animateTo(target, animationSpec = tween(300))
                                    }
                                }
                            )
                        }
                ) {
                    if (offsetY.value < screenHeightPx * 0.3f && uiState.weather != null) {
                        WeatherDetail(uiState.weather, selectedLanguage)
                    } else {
                        ForecastListCard(
                            hourlyWeatherInfos = uiState.weather?.hourlyForecast.orEmpty(),
                            weeklyWeatherInfos = uiState.weather?.dailyForecast.orEmpty(),
                            isLoading = uiState.isLoading,
                            paddingValues = PaddingValues(
                                top = XLargeSpacing
                            ),
                            horizontalPadding = PaddingValues(horizontal = 20.dp),
                            language = selectedLanguage
                        )
                    }
                }

                BottomBar(
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                    onAddClick = {
                        navigator.push(SearchScreen {
                            viewModel.getPreferredCity()
                        })
                    },
                    onSettingsClick = {
                        navigator.push(SettingsScreen())
                    },
                    isDarkTheme = themeStyle == ThemeStyle.Dark,
                    onPositionClick = {
                        viewModel.onLocationUpdate(null)
                        model.currentLocation()
                    })
            }
        }
    }
}