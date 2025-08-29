package com.dellapp.weatherapp.feature.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dellapp.weatherapp.core.common.EndGradientBg
import com.dellapp.weatherapp.core.common.StartGradientBg
import com.dellapp.weatherapp.core.common.geolocation.GeolocationModel
import com.dellapp.weatherapp.core.common.geolocation.createGeolocator
import com.dellapp.weatherapp.core.ui.components.GradientBox
import com.dellapp.weatherapp.feature.home.ui.HomeScreen
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.stateholder.extensions.collectAsState
import org.jetbrains.compose.resources.painterResource
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.app_logo

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = rememberScreenModel<GeolocationModel> {
            GeolocationModel(createGeolocator())
        }
        val state by model.collectAsState()

        LaunchedEffect(state.location, state.lastResult) {
            when {
                state.location == null && state.lastResult !is GeolocatorResult.PermissionDenied && state.lastResult !is GeolocatorResult.NotSupported -> {
                    model.currentLocation()
                }
                else -> {
                    navigator.push(HomeScreen(model))
                }
            }
        }

        GradientBox(
            colors = listOf(StartGradientBg, EndGradientBg),
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(128.dp)
            )
        }
    }
}