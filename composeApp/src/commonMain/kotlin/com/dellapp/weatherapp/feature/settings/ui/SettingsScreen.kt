package com.dellapp.weatherapp.feature.settings.ui

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.dellapp.weatherapp.core.common.EndGradientBg
import com.dellapp.weatherapp.core.common.Language
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.StartGradientBg
import com.dellapp.weatherapp.core.ui.components.GradientBox
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.language
import weatherapp.composeapp.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    selectedLanguage: Language,
    onLanguageSelected: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsViewModel.SettingsEvent.NavigateBack -> {
                    onLanguageSelected()
                }
            }
        }
    }

    GradientBox(
        colors = listOf(StartGradientBg, EndGradientBg),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(WindowInsets.safeContent.asPaddingValues()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(Res.string.settings),
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp)
            )
            Spacer(Modifier.height(LargeSpacing))
            if(uiState.isLoading) {
                CircularProgressIndicator()
            } else if(uiState.error != null) {

            } else {
                LanguageSelectorRow(
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = { language ->
                        viewModel.setPreferredLanguage(language.iso)
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageSelectorRow(
    modifier: Modifier = Modifier,
    selectedLanguage: Language,
    onLanguageSelected: (Language) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var currentLanguage by remember { mutableStateOf(selectedLanguage) }

    val languages = Language.entries.toList()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(horizontal = LargeSpacing, vertical = MediumSpacing),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.language),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )

        Box {
            Text(
                text = currentLanguage.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = language.name,
                                color = if (language == currentLanguage) MaterialTheme.colorScheme.primary else Color.Unspecified
                            )
                        },
                        onClick = {
                            currentLanguage = language
                            expanded = false
                            onLanguageSelected(language)
                        }
                    )
                }
            }
        }
    }
}
