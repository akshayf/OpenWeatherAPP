package com.example.openweatherapp.remote

import com.example.openweatherapp.data.LocationModel
import com.example.openweatherapp.data.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getCityWeather(
        @Query("appid") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ): Response<WeatherModel>

    @GET("direct")
    suspend fun getCityLocation(
        @Query("appid") apiKey: String,
        @Query("q") city: String,
        @Query("limit") limit: Int = 1
    ): Response<LocationModel>

}