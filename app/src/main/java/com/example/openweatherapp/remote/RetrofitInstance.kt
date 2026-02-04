package com.example.openweatherapp.remote

import com.example.openweatherapp.utils.LoggerUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private fun getInstance(baseUrl: String): Retrofit {

        LoggerUtil.debug("BaseURL: $baseUrl")

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * API for getting instance of Retrofit
     */
    fun weatherApi(baseUrl: String): WeatherApi {
        return getInstance(baseUrl).create(WeatherApi::class.java)
    }


}