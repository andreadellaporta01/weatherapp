package com.dellapp.weatherapp

import androidx.compose.ui.window.ComposeUIViewController
import com.dellapp.weatherapp.core.common.Context

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin(Context())
    }
) { App() }