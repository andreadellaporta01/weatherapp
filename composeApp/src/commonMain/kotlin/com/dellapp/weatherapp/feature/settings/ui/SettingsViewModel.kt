package com.dellapp.weatherapp.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dellapp.weatherapp.core.domain.usecase.SetLanguageUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val setLanguageUseCase: SetLanguageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()

    fun setPreferredLanguage(language: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        setLanguageUseCase(language)
            .onSuccess {
                _events.emit(SettingsEvent.LanguageSelected)
            }
            .onFailure { exception ->
                _uiState.update { it.copy(error = exception.message) }
            }
        _uiState.update { it.copy(isLoading = false) }
    }

    sealed class SettingsEvent {
        object LanguageSelected : SettingsEvent()
    }
}