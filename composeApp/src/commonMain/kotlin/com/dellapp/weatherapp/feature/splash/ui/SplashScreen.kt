package com.dellapp.weatherapp.feature.splash.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.dellapp.weatherapp.core.common.EndGradientBg
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.StartGradientBg
import com.dellapp.weatherapp.core.common.XXLargeSpacing
import com.dellapp.weatherapp.core.common.geolocation.GeolocationModel
import com.dellapp.weatherapp.core.common.geolocation.canShowAppSettings
import com.dellapp.weatherapp.core.common.geolocation.createGeolocator
import com.dellapp.weatherapp.core.common.geolocation.showAppSettings
import com.dellapp.weatherapp.core.ui.components.GradientBox
import com.dellapp.weatherapp.feature.home.ui.HomeScreen
import dev.stateholder.extensions.collectAsState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.current_position
import weatherapp.composeapp.generated.resources.ic_pin
import weatherapp.composeapp.generated.resources.location_permission_required
import weatherapp.composeapp.generated.resources.open_settings

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = rememberScreenModel<GeolocationModel> {
            GeolocationModel(createGeolocator())
        }
        val state by model.collectAsState()

        LaunchedEffect(state.location) {
            navigator.push(HomeScreen(model))
        }

        GradientBox(
            colors = listOf(StartGradientBg, EndGradientBg),
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = Res.getUri("drawable/ic_pin.svg"),
                contentDescription = "App Logo",
                modifier = Modifier.size(128.dp)
            )
        }
    }
}