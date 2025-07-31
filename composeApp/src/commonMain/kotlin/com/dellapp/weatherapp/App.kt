package com.dellapp.weatherapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dellapp.weatherapp.core.common.AppDataStore
import com.dellapp.weatherapp.core.common.AppDataStoreManager
import com.dellapp.weatherapp.core.common.Context
import com.dellapp.weatherapp.core.common.Localization
import com.dellapp.weatherapp.core.common.Theme
import com.dellapp.weatherapp.core.data.di.coreDataModule
import com.dellapp.weatherapp.core.domain.di.coreDomainModule
import com.dellapp.weatherapp.core.ui.CoreViewModel
import com.dellapp.weatherapp.core.ui.di.corePresentationModule
import com.dellapp.weatherapp.feature.home.domain.di.homeDomainModule
import com.dellapp.weatherapp.feature.home.ui.HomeRoute
import com.dellapp.weatherapp.feature.home.ui.HomeScreen
import com.dellapp.weatherapp.feature.home.ui.di.homePresentationModule
import com.dellapp.weatherapp.feature.search.domain.di.searchDomainModule
import com.dellapp.weatherapp.feature.search.ui.SearchRoute
import com.dellapp.weatherapp.feature.search.ui.SearchScreen
import com.dellapp.weatherapp.feature.search.ui.di.searchPresentationModule
import com.dellapp.weatherapp.feature.settings.ui.SettingsRoute
import com.dellapp.weatherapp.feature.settings.ui.SettingsScreen
import com.dellapp.weatherapp.feature.settings.ui.di.settingsPresentationModule
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
    val navController = rememberNavController()
    val localization = koinInject<Localization>()
    val selectedLanguage by coreViewModel.selectedLanguage.collectAsState()

    LaunchedEffect(Unit) {
        coreViewModel.selectedLanguage.collect { language ->
            localization.applyLanguage(selectedLanguage.iso)
        }
    }

    Theme {
        NavHost(
            navController = navController,
            startDestination = HomeRoute
        ) {
            composable<HomeRoute> { backStackEntry ->
                val savedStateHandle = backStackEntry.savedStateHandle
                val refreshTrigger = savedStateHandle.get<Boolean>("refresh_trigger") ?: false
                HomeScreen(
                    refreshTrigger = refreshTrigger,
                    selectedLanguage = selectedLanguage,
                    onNavigateToSearch = {
                        navController.navigate(SearchRoute)
                    },
                    onNavigateToSettings = {
                        navController.navigate(SettingsRoute)
                    }
                )
            }
            composable<SearchRoute> {
                SearchScreen(
                    onCitySelected = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("refresh_trigger", true)
                        navController.popBackStack()
                    }
                )
            }
            composable<SettingsRoute> {
                SettingsScreen(
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = {
                       coreViewModel.getLanguage()
                    }
                )
            }
        }
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
            settingsPresentationModule
        )
    }
}