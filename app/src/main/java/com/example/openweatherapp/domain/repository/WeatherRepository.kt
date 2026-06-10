package com.example.openweatherapp.domain.repository

import com.example.openweatherapp.data.remote.NetworkResponse
import com.example.openweatherapp.data.remote.dto.WeatherModel

import androidx.paging.PagingData
import com.example.openweatherapp.data.remote.dto.LocationModelItem
import kotlinx.coroutines.flow.Flow

/**
 * Domain-level repository interface for weather and city data.
 */
interface WeatherRepository {
    /** Fetches weather for a specific city. */
    suspend fun getWeather(city: String): NetworkResponse<WeatherModel>
    /** Provides a paged flow of cities matching the query. */
    fun searchCities(query: String): Flow<PagingData<LocationModelItem>>
}
