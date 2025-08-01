package com.dellapp.weatherapp.feature.search.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dellapp.weatherapp.core.common.EndGradientBg
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.SmallSpacing
import com.dellapp.weatherapp.core.common.StartGradientBg
import com.dellapp.weatherapp.core.ui.components.GradientBox
import com.dellapp.weatherapp.core.ui.components.SearchBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.back
import weatherapp.composeapp.generated.resources.city_not_found
import weatherapp.composeapp.generated.resources.weather

class SearchScreen(
    private val onCitySelected: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val viewModel: SearchViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val uiState by viewModel.uiState.collectAsState()
        var searchText by remember { mutableStateOf("") }
        var showSuggestions by remember { mutableStateOf(false) }

        LaunchedEffect(searchText) {
            if (searchText.length >= 3) {
                viewModel.searchCity(searchText)
            }
        }

        GradientBox(
            modifier = Modifier.fillMaxSize(),
            colors = listOf(StartGradientBg, EndGradientBg),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(WindowInsets.safeContent.asPaddingValues()),
            ) {
                Spacer(Modifier.height(MediumSpacing))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back),
                        tint = Color.White,
                        modifier = Modifier.clickable { navigator.pop() }
                    )
                    Spacer(modifier = Modifier.width(MediumSpacing))
                    Text(
                        stringResource(Res.string.weather),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(LargeSpacing))
                    SearchBar(
                        query = searchText,
                        onQueryChange = {
                            searchText = it
                            showSuggestions = it.length >= 3
                        },
                    )
                    Spacer(modifier = Modifier.height(SmallSpacing))
                    if (showSuggestions) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator()
                        } else if (uiState.cities.isNullOrEmpty()) {
                            Text(
                                text = stringResource(Res.string.city_not_found),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else if (uiState.isPreferredCitySet) {
                            onCitySelected()
                            navigator.pop()
                        } else {
                            LazyColumn {
                                items(uiState.cities!!.count()) { position ->
                                    SuggestionItem(
                                        text = uiState.cities!![position].name,
                                        onClick = { selected ->
                                            searchText = selected
                                            viewModel.setPreferredCity(uiState.cities!![position])
                                        })
                                }
                            }
                        }
                    }
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
            Text(text = text, color = Color.White)
        }
    }
}