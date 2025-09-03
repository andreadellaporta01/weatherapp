package com.dellapp.weatherapp.core.data.local

import com.dellapp.weatherapp.core.common.Context
import com.dellapp.weatherapp.core.domain.repository.AppDataStoreRepository

const val APP_DATASTORE = "com.dellapp.weatherapp"

class AppDataStoreRepositoryManager(val context: Context?) : AppDataStoreRepository {

    override suspend fun setValue(
        key: String,
        value: String
    ) {
        context.putData(key, value)
    }

    override suspend fun readValue(
        key: String,
    ): String? {
        return context.getData(key)
    }
}

expect suspend fun Context?.putData(key: String, `object`: String)

expect suspend fun Context?.getData(key: String): String?