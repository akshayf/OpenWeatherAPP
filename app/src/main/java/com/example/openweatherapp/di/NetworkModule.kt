package com.example.openweatherapp.di

import com.example.openweatherapp.BuildConfig
import com.example.openweatherapp.data.remote.api.WeatherApi
import com.example.openweatherapp.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Dagger Module for providing network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /** Provides the AuthInterceptor. */
    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }

    /** Provides the common OkHttpClient. */
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    /** Provides Retrofit for weather data. */
    @Provides
    @Singleton
    @Named("weather")
    fun provideWeatherRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Provides Retrofit for location data. */
    @Provides
    @Singleton
    @Named("location")
    fun provideLocationRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.LOCATION_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Provides the WeatherApi instance. */
    @Provides
    @Singleton
    fun provideWeatherApi(@Named("weather") retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    /** Provides a separate WeatherApi instance for location queries. */
    @Provides
    @Singleton
    @Named("locationApi")
    fun provideLocationApi(@Named("location") retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}
