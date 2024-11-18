package com.example.weatherapp.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "favorite_cities")

class DataStoreManager(private val context: Context) {


    suspend fun addFavoriteCity(name: String, lat: Float, lon: Float) {
        val existingCities = getFavoriteCities().map { cities ->
            cities.any { it.name == name }
        }.first()

        if (!existingCities) {
            context.dataStore.edit { preferences ->
                preferences[stringPreferencesKey("$name-cityName")] = name
                preferences[floatPreferencesKey("$name-latitude")] = lat
                preferences[floatPreferencesKey("$name-longitude")] = lon
            }
        }
    }

    fun getFavoriteCities(): Flow<List<FavoriteCity>> {
        return context.dataStore.data.map { preferences ->
            preferences.asMap().entries.mapNotNull { entry ->
                val cityName = entry.key.name.split("-").firstOrNull()
                val lat = preferences[floatPreferencesKey("$cityName-latitude")]
                val lon = preferences[floatPreferencesKey("$cityName-longitude")]
                if (cityName != null && lat != null && lon != null) {
                    FavoriteCity(cityName, lat, lon)
                } else null
            }
        }
    }

    suspend fun removeFavoriteCity(name: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey("$name-cityName"))
            preferences.remove(floatPreferencesKey("$name-latitude"))
            preferences.remove(floatPreferencesKey("$name-longitude"))
        }
    }
}
