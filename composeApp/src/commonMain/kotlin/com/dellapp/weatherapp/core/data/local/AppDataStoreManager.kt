package com.dellapp.weatherapp.core.data.local

import com.dellapp.weatherapp.core.common.Context

const val APP_DATASTORE = "com.dellapp.weatherapp"

class AppDataStoreManager(val context: Context?) : AppDataStore {

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