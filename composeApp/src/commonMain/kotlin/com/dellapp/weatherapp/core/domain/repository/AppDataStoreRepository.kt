package com.dellapp.weatherapp.core.domain.repository

import com.dellapp.weatherapp.core.common.Context

interface AppDataStoreRepository {

    suspend fun setValue(
        key: String,
        value: String
    )

    suspend fun readValue(
        key: String,
    ): String?

}