package com.dellapp.weatherapp.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dellapp.weatherapp.core.common.Language
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.Localization
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.Platform
import com.dellapp.weatherapp.core.common.SmallSpacing
import com.dellapp.weatherapp.core.common.ThemeStyle
import com.dellapp.weatherapp.core.common.getCurrentPlatform
import com.dellapp.weatherapp.core.common.getScreenPaddingValues
import com.dellapp.weatherapp.core.ui.CoreViewModel
import com.dellapp.weatherapp.core.ui.components.GradientBox
import com.dellapp.weatherapp.core.ui.components.SelectorRow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.back
import weatherapp.composeapp.generated.resources.language
import weatherapp.composeapp.generated.resources.settings
import weatherapp.composeapp.generated.resources.theme

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val coreViewModel: CoreViewModel = getKoin().get()
        val viewModel: SettingsViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val uiState by viewModel.uiState.collectAsState()
        val selectedLanguage by coreViewModel.selectedLanguage.collectAsState()
        val selectedTheme by coreViewModel.selectedThemeStyle.collectAsState()
        val isSystemDark = isSystemInDarkTheme()
        val themeStyle = selectedTheme
            ?: (if (isSystemDark) ThemeStyle.Dark else ThemeStyle.Light)
        val snackbarHostState = remember { SnackbarHostState() }
        val localization = koinInject<Localization>()

        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                when (event) {
                    is SettingsViewModel.SettingsEvent.LanguageSelected -> {
                        localization.applyLanguage(event.language.iso)
                        coreViewModel.getLanguage()
                    }

                    is SettingsViewModel.SettingsEvent.ThemeSelected -> {
                        coreViewModel.getTheme()
                    }
                }
            }
        }

        LaunchedEffect(uiState.error) {
            uiState.error?.let { error ->
                snackbarHostState.showSnackbar(message = error)
            }
        }
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data: SnackbarData ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            },
            containerColor = Color.Transparent
        ) {
            GradientBox(
                modifier = Modifier.fillMaxSize(),
                colors = listOf(
                    MaterialTheme.colorScheme.surfaceContainerHigh,
                    MaterialTheme.colorScheme.surfaceContainerLow
                ),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(getScreenPaddingValues()),
                ) {
                    Spacer(Modifier.height(MediumSpacing))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { navigator.pop() }
                        )
                        Spacer(modifier = Modifier.width(MediumSpacing))
                        Text(
                            stringResource(Res.string.settings),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(LargeSpacing))
                        if (uiState.isLoading) {
                            CircularProgressIndicator()
                        } else {
                            if (getCurrentPlatform() != Platform.WASM) {
                                SelectorRow(
                                    currentValue = selectedLanguage,
                                    title = stringResource(Res.string.language),
                                    values = Language.entries,
                                    onSelected = { language ->
                                        viewModel.setPreferredLanguage(language.iso)
                                    }
                                )
                                Spacer(Modifier.height(SmallSpacing))
                            }
                            SelectorRow(
                                currentValue = themeStyle,
                                title = stringResource(Res.string.theme),
                                values = ThemeStyle.entries,
                                onSelected = { theme ->
                                    viewModel.setPreferredTheme(theme.theme)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
