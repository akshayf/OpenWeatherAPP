package com.example.openweatherapp.domain.usecase

import com.example.openweatherapp.data.remote.dto.WeatherModel
import com.example.openweatherapp.domain.repository.WeatherRepository
import com.example.openweatherapp.data.remote.NetworkResponse
import javax.inject.Inject

/**
 * Use case to fetch weather data for a specific city.
 */
class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): NetworkResponse<WeatherModel> {
        return repository.getWeather(city)
    }
}
