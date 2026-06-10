package com.example.openweatherapp.data.remote.api

import com.example.openweatherapp.data.remote.dto.LocationModel
import com.example.openweatherapp.data.remote.dto.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for OpenWeather API endpoints.
 */
interface WeatherApi {

    /**
     * Fetches weather data for specific coordinates.
     */
    @GET("weather")
    suspend fun getCityWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ): Response<WeatherModel>

    /**
     * Fetches location coordinates for a given city name.
     */
    @GET("direct")
    suspend fun getCityLocation(
        @Query("q") city: String,
        @Query("limit") limit: Int = 1
    ): Response<LocationModel>

}