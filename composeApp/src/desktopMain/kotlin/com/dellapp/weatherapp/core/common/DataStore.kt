package com.dellapp.weatherapp.core.common

import java.io.File
import java.util.Properties


private val preferencesDir = File(System.getProperty("user.home"), ".weatherapp").apply { mkdirs() }
private val file = File(preferencesDir, "preferences.properties")
private val properties = Properties()

actual suspend fun Context?.getData(key: String): String? {
    if (file.exists()) {
        file.inputStream().use { properties.load(it) }
    }
    return properties.getProperty(key)
}

actual suspend fun Context?.putData(key: String, `object`: String) {
    properties[key] = `object`
    file.outputStream().use { properties.store(it, null) }
}

