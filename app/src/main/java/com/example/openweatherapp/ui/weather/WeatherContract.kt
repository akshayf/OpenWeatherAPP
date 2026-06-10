package com.example.openweatherapp.ui.weather

import com.example.openweatherapp.data.remote.dto.WeatherModel

import androidx.paging.PagingData
import com.example.openweatherapp.data.remote.dto.LocationModelItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Represents the user intents for the Weather screen.
 */
sealed class WeatherIntent {
    data class FetchWeather(val city: String) : WeatherIntent()
    data class RefreshWeather(val city: String) : WeatherIntent()
    data class SearchCities(val query: String) : WeatherIntent()
    object ClearSearchResults : WeatherIntent()
}

/**
 * Represents the UI state for the Weather screen.
 */
data class WeatherState(
    val weatherData: WeatherModel? = null,
    val weatherIconUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val cityName: String = "",
    val isOnline: Boolean = true,
    val searchResults: Flow<PagingData<LocationModelItem>> = emptyFlow(),
)
