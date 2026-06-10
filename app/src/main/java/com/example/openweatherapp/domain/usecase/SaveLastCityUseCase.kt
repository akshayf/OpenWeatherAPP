package com.example.openweatherapp.domain.usecase

import com.example.openweatherapp.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Use case to save the last searched city to settings.
 */
class SaveLastCityUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(city: String) {
        repository.saveCity(city)
    }
}
