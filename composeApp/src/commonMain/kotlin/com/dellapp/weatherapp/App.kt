package com.dellapp.weatherapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.AppDataStoreManager
import com.dellapp.weatherapp.core.common.Context
import com.dellapp.weatherapp.core.common.Localization
import com.dellapp.weatherapp.core.common.Theme
import com.dellapp.weatherapp.core.common.ThemeStyle
import com.dellapp.weatherapp.core.data.di.coreDataModule
import com.dellapp.weatherapp.core.domain.di.coreDomainModule
import com.dellapp.weatherapp.core.ui.CoreViewModel
import com.dellapp.weatherapp.core.ui.di.corePresentationModule
import com.dellapp.weatherapp.feature.home.domain.di.homeDomainModule
import com.dellapp.weatherapp.feature.home.ui.di.homePresentationModule
import com.dellapp.weatherapp.feature.search.domain.di.searchDomainModule
import com.dellapp.weatherapp.feature.search.ui.di.searchPresentationModule
import com.dellapp.weatherapp.feature.settings.domain.di.settingsDomainModule
import com.dellapp.weatherapp.feature.settings.ui.di.settingsPresentationModule
import com.dellapp.weatherapp.feature.splash.ui.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

@Composable
@Preview
fun App() {
    val coreViewModel: CoreViewModel = koinViewModel()
    val localization = koinInject<Localization>()
    val selectedLanguage by coreViewModel.selectedLanguage.collectAsState()
    val selectedTheme by coreViewModel.selectedThemeStyle.collectAsState()
    val systemInDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemInDarkTheme) }

    LaunchedEffect(selectedLanguage) {
        localization.applyLanguage(selectedLanguage.iso)
    }

    LaunchedEffect(selectedTheme) {
        isDarkTheme = selectedTheme == ThemeStyle.Dark
    }

    Theme(
        darkTheme = isDarkTheme
    ) {
        Navigator(
            screen = SplashScreen()
        )
    }
}

expect val targetModule: Module

fun initKoin(context: Context?, config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        val dataStoreModule = module {
            single<AppDataStore> { AppDataStoreManager(context) }
        }
        modules(
            targetModule,
            dataStoreModule,
            coreDataModule,
            coreDomainModule,
            corePresentationModule,
            homeDomainModule,
            homePresentationModule,
            searchDomainModule,
            searchPresentationModule,
            settingsDomainModule,
            settingsPresentationModule
        )
    }
}