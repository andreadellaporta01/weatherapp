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
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.svg.SvgDecoder
import com.dellapp.weatherapp.core.data.local.AppDataStore
import com.dellapp.weatherapp.core.data.local.AppDataStoreManager
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
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

@Composable
@Preview
fun App() {
    val coreViewModel: CoreViewModel = getKoin().get()
    val selectedTheme by coreViewModel.selectedThemeStyle.collectAsState()
    val systemInDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemInDarkTheme) }
    val platformContext = LocalPlatformContext.current
    val selectedLanguage by coreViewModel.selectedLanguage.collectAsState()
    val localization = koinInject<Localization>()

    LaunchedEffect(selectedTheme) {
        if(selectedTheme != null) {
            isDarkTheme = selectedTheme == ThemeStyle.Dark
        }
    }

    LaunchedEffect(selectedLanguage) {
        print(selectedLanguage.iso)
        localization.applyLanguage(selectedLanguage.iso)
    }

    setSingletonImageLoaderFactory {
        ImageLoader.Builder(platformContext)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
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