package com.dellapp.weatherapp.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dellapp.weatherapp.core.common.Language
import com.dellapp.weatherapp.core.common.ThemeStyle
import com.dellapp.weatherapp.core.domain.usecase.SetLanguageUseCase
import com.dellapp.weatherapp.feature.settings.domain.SetThemeUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()

    fun setPreferredLanguage(language: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        setLanguageUseCase(language)
            .onSuccess {
                _events.emit(SettingsEvent.LanguageSelected(Language.fromIso(language)))
            }
            .onFailure { exception ->
                _uiState.update { it.copy(error = exception.message) }
            }
        _uiState.update { it.copy(isLoading = false) }
    }

    fun setPreferredTheme(theme: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        setThemeUseCase(theme)
            .onSuccess {
                _events.emit(SettingsEvent.ThemeSelected(ThemeStyle.fromTheme(theme)))
            }
            .onFailure { exception ->
                _uiState.update { it.copy(error = exception.message) }
            }
        _uiState.update { it.copy(isLoading = false) }
    }

    sealed class SettingsEvent {
        class LanguageSelected(val language: Language) : SettingsEvent()
        class ThemeSelected(val themeStyle: ThemeStyle) : SettingsEvent()
    }
}