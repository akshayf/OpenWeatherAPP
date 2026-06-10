package com.example.openweatherapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.openweatherapp.data.remote.NetworkResponse
import com.example.openweatherapp.data.remote.api.WeatherApi
import com.example.openweatherapp.data.remote.dto.LocationModelItem
import com.example.openweatherapp.data.remote.dto.WeatherModel
import com.example.openweatherapp.data.remote.paging.CityPagingSource
import com.example.openweatherapp.domain.repository.WeatherRepository
import com.example.openweatherapp.utils.ResourceProvider
import com.example.openweatherapp.R
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val resourceProvider: ResourceProvider,
    @Named("locationApi") private val locationApi: WeatherApi
) : WeatherRepository {

    /**
     * Fetches weather data for a city by first resolving its coordinates.
     */
    override suspend fun getWeather(city: String): NetworkResponse<WeatherModel> {
        return try {
            val locationResponse = locationApi.getCityLocation(city)
            if (locationResponse.isSuccessful) {
                val locationModel = locationResponse.body()
                if (locationModel != null && locationModel.isNotEmpty()) {
                    val lat = locationModel[0].lat
                    val lon = locationModel[0].lon
                    val weatherResponse = weatherApi.getCityWeather(lat, lon)
                    if (weatherResponse.isSuccessful) {
                        weatherResponse.body()?.let {
                            NetworkResponse.Success(it)
                        } ?: NetworkResponse.Error(resourceProvider.getString(R.string.error_empty_response))
                    } else {
                        NetworkResponse.Error(resourceProvider.getString(R.string.error_fetching_weather, weatherResponse.message()))
                    }
                } else {
                    NetworkResponse.Error(resourceProvider.getString(R.string.error_city_not_found))
                }
            } else {
                NetworkResponse.Error(resourceProvider.getString(R.string.error_fetching_location, locationResponse.message()))
            }
        } catch (e: Exception) {
            NetworkResponse.Error(resourceProvider.getString(R.string.error_exception, e.message ?: "Unknown"))
        }
    }

    /**
     * Returns a PagingData flow for city search results.
     */
    override fun searchCities(query: String): Flow<PagingData<LocationModelItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { CityPagingSource(locationApi, query) }
        ).flow
    }
}
