package com.dellapp.weatherapp.core.common

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