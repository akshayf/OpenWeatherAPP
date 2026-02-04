package com.example.openweatherapp.di

import android.content.Context
import com.example.openweatherapp.repository.SettingsRepository
import com.example.openweatherapp.utils.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return ConnectivityObserver(context)
    }

    @Singleton
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepository(context)
    }
}