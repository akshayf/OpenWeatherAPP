package com.example.openweatherapp.model

import app.cash.turbine.test
import com.example.openweatherapp.remote.NetworkResponse
import com.example.openweatherapp.repository.SettingsRepository
import com.example.openweatherapp.utils.ConnectivityObserver
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: SettingsRepository
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        connectivityObserver = mockk(relaxed = true)
        
        every { connectivityObserver.isConnected } returns flowOf(false)
        every { repository.lastCityFlow } returns flowOf("Loading...")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isOnline flow reflects connectivity observer status`() = runTest {
        val isConnectedFlow = MutableStateFlow(false)
        every { connectivityObserver.isConnected } returns isConnectedFlow
        
        viewModel = WeatherViewModel(repository, connectivityObserver)
        
        viewModel.isOnline.test {
            assertEquals(false, awaitItem()) // Initial value
            
            isConnectedFlow.value = true
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `getCityData updates result to Error when offline`() = runTest {
        every { connectivityObserver.isConnected } returns flowOf(false)
        viewModel = WeatherViewModel(repository, connectivityObserver)

        viewModel.getCityData("London")

        viewModel.locationResult.test {
            var item = awaitItem()
            if (item is NetworkResponse.NullCheck) {
                item = awaitItem()
            }
            assertTrue(item is NetworkResponse.Error)
            assertEquals("No Internet Connection", (item as NetworkResponse.Error).message)
        }
    }

    @Test
    fun `initial fetch occurs when city is loaded and online`() = runTest {
        every { connectivityObserver.isConnected } returns flowOf(true)
        every { repository.lastCityFlow } returns flowOf("Paris")
        
        viewModel = WeatherViewModel(repository, connectivityObserver)

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.locationResult.value is NetworkResponse.Loading)
    }
}
