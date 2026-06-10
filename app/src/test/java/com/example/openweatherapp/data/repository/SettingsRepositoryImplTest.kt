package com.example.openweatherapp.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SettingsRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var repository: SettingsRepositoryImpl

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = SettingsRepositoryImpl(context)
    }

    @Test
    fun `lastCityFlow emits default city initially`() = runTest {
        repository.lastCityFlow.test {
            assertEquals("New York", awaitItem())
        }
    }

    @Test
    fun `saveCity updates lastCityFlow`() = runTest {
        val newCity = "London"
        repository.saveCity(newCity)
        
        repository.lastCityFlow.test {
            assertEquals(newCity, awaitItem())
        }
    }
}
