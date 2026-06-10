package com.example.openweatherapp.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Domain-level repository interface for persistent settings.
 */
interface SettingsRepository {
    /** Flow of the last searched city. */
    val lastCityFlow: Flow<String>
    /** Saves the last searched city. */
    suspend fun saveCity(city: String)
}
