package com.dellapp.weatherapp.core.common

interface AppDataStore {

    suspend fun setValue(
        key: String,
        value: String
    )

    suspend fun readValue(
        key: String,
    ): String?

}

expect suspend fun Context?.putData(key: String, `object`: String)

expect suspend fun Context?.getData(key: String): String?