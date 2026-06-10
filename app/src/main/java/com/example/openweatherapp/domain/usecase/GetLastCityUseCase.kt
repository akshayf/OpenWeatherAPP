package com.example.openweatherapp.domain.usecase

import com.example.openweatherapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve the last searched city from settings.
 */
class GetLastCityUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<String> = repository.lastCityFlow
}
