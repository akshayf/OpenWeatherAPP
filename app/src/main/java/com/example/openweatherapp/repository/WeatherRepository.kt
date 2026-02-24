package com.example.openweatherapp.repository

import com.example.openweatherapp.data.LocationModel
import com.example.openweatherapp.data.WeatherModel
import com.example.openweatherapp.remote.NetworkResponse
import com.example.openweatherapp.remote.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val locationApi: WeatherApi
) {

    suspend fun getWeather(city: String): NetworkResponse<WeatherModel> {
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
                        } ?: NetworkResponse.Error("Empty weather response")
                    } else {
                        NetworkResponse.Error("Error fetching weather: ${weatherResponse.message()}")
                    }
                } else {
                    NetworkResponse.Error("City not found")
                }
            } else {
                NetworkResponse.Error("Error fetching location: ${locationResponse.message()}")
            }
        } catch (e: Exception) {
            NetworkResponse.Error("Exception: ${e.message}")
        }
    }
}
