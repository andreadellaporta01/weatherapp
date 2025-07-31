package com.dellapp.weatherapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(context = null) {
        modules()
    }
    ComposeViewport(document.body!!) {
        App()
    }
}