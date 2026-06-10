package com.example.openweatherapp.di

import com.example.openweatherapp.data.repository.SettingsRepositoryImpl
import com.example.openweatherapp.data.repository.WeatherRepositoryImpl
import com.example.openweatherapp.domain.repository.SettingsRepository
import com.example.openweatherapp.domain.repository.WeatherRepository
import com.example.openweatherapp.utils.ResourceProvider
import com.example.openweatherapp.utils.ResourceProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Module for binding repository implementations to their interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /** Binds WeatherRepository implementation. */
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    /** Binds SettingsRepository implementation. */
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    /** Binds ResourceProvider implementation. */
    @Binds
    @Singleton
    abstract fun bindResourceProvider(
        resourceProviderImpl: ResourceProviderImpl
    ): ResourceProvider
}
