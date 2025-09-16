package com.dellapp.weatherapp.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dellapp.weatherapp.core.common.Language
import com.dellapp.weatherapp.core.common.ThemeStyle
import com.dellapp.weatherapp.core.domain.usecase.GetLanguageUseCase
import com.dellapp.weatherapp.core.domain.usecase.GetThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class CoreViewModel(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getThemeUseCase: GetThemeUseCase
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow(Language.English)
    open val selectedLanguage = _selectedLanguage.asStateFlow()


    private val _selectedThemeStyle = MutableStateFlow<ThemeStyle?>(null)
    open val selectedThemeStyle = _selectedThemeStyle.asStateFlow()

    init {
        getLanguage()
        getTheme()
    }

    fun getLanguage() = viewModelScope.launch {
        getLanguageUseCase()
            .onSuccess { language ->
                _selectedLanguage.value =
                    Language.entries.firstOrNull { it.iso == language } ?: Language.English
            }
            .onFailure {
                _selectedLanguage.value = Language.English // Default
            }
    }

    fun getTheme() = viewModelScope.launch {
        getThemeUseCase()
            .onSuccess { theme ->
                _selectedThemeStyle.value = ThemeStyle.entries.firstOrNull { it.theme == theme }
            }
            .onFailure {
                _selectedThemeStyle.value = null // Default
            }
    }
}
