package com.dellapp.weatherapp.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dellapp.weatherapp.core.common.Language
import com.dellapp.weatherapp.core.domain.usecase.GetLanguageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoreViewModel(
    private val getLanguageUseCase: GetLanguageUseCase,
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow(Language.English)
    val selectedLanguage = _selectedLanguage.asStateFlow()

    init {
        getLanguage()
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
}
