package com.example.openweatherapp.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository(private val context: Context) {
    private val CITY_KEY = stringPreferencesKey("last_city")

    // Read the city as a Flow
    val lastCityFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[CITY_KEY] ?: "New York" // Default to New York
        }

    // Save the last searched city
    suspend fun saveCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[CITY_KEY] = city
        }
    }
}
