package com.example.openweatherapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.openweatherapp.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

/**
 * DataStore implementation for persisting user settings like the last searched city.
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {
    private val CITY_KEY = stringPreferencesKey("last_city")

    /**
     * Flow that emits the last saved city name.
     */
    override val lastCityFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[CITY_KEY] ?: "New York"
        }

    /**
     * Saves the city name to DataStore.
     */
    override suspend fun saveCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[CITY_KEY] = city
        }
    }
}
