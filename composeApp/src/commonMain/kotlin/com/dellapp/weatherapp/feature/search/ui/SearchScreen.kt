package com.dellapp.weatherapp.feature.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.SmallSpacing
import com.dellapp.weatherapp.core.common.XLargeSpacing
import com.dellapp.weatherapp.core.ui.components.GradientBox
import com.dellapp.weatherapp.core.ui.components.SearchBar
import com.dellapp.weatherapp.feature.search.ui.favorite.FavoriteCityCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.back
import weatherapp.composeapp.generated.resources.city_not_found
import weatherapp.composeapp.generated.resources.delete
import weatherapp.composeapp.generated.resources.weather

class SearchScreen(
    private val onCitySelected: () -> Unit
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel: SearchViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val uiState by viewModel.uiState.collectAsState()
        var searchText by remember { mutableStateOf("") }
        var showSuggestions by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            viewModel.getPreferredCities()
        }

        LaunchedEffect(searchText) {
            if (searchText.length >= 3) {
                viewModel.searchCity(searchText)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                when (event) {
                    is SearchViewModel.SearchEvent.PreferredCitySelected -> {
                        onCitySelected()
                        navigator.pop()
                    }
                }
            }
        }

        GradientBox(
            modifier = Modifier.fillMaxSize(),
            colors = listOf(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                MaterialTheme.colorScheme.surfaceContainerLow
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.safeContent.asPaddingValues())
            ) {
                Spacer(Modifier.height(MediumSpacing))
                Row(
                    modifier = Modifier.padding(horizontal = MediumSpacing),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { navigator.pop() }
                    )
                    Spacer(modifier = Modifier.width(MediumSpacing))
                    Text(
                        stringResource(Res.string.weather),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp)
                    )
                }
                Spacer(Modifier.height(LargeSpacing))
                SearchBar(
                    query = searchText,
                    onQueryChange = {
                        searchText = it
                        showSuggestions = it.length >= 3
                    },
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MediumSpacing, vertical = MediumSpacing)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(XLargeSpacing)
                ) {
                    if (showSuggestions) {
                        if (uiState.isSuggestionLoading) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            }
                        } else if (uiState.cities.isNullOrEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(Res.string.city_not_found),
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        } else {
                            uiState.cities!!.forEach { city ->
                                SuggestionItem(
                                    text = "${city.name}, ${city.country}",
                                    onClick = { selected ->
                                        searchText = selected
                                        viewModel.setPreferredCity(city)
                                    }
                                )
                            }
                        }
                    } else {
                        uiState.preferredCities?.forEach { city ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                positionalThreshold = { totalDistance -> totalDistance * 0.5f },
                                confirmValueChange = {
                                    if (it == SwipeToDismissBoxValue.EndToStart) {
                                        viewModel.deletePreferredCity(city)
                                    }
                                    true
                                }
                            )
                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(end = 24.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = stringResource(Res.string.delete),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            ) {
                                FavoriteCityCard(
                                    city = city,
                                    onCitySelected = {
                                        viewModel.setPreferredCity(it)
                                    }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(XLargeSpacing))
                }
            }
        }
    }

    @Composable
    private fun SuggestionItem(text: String, onClick: (String) -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(text) }
                .padding(vertical = SmallSpacing, horizontal = MediumSpacing)
        ) {
            Text(text = text, color = MaterialTheme.colorScheme.primary)
        }
    }
}