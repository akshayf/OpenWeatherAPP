package com.example.openweatherapp.model

import app.cash.turbine.test
import com.example.openweatherapp.BuildConfig
import com.example.openweatherapp.data.LocationModel
import com.example.openweatherapp.data.LocationModelItem
import com.example.openweatherapp.data.WeatherModel
import com.example.openweatherapp.remote.NetworkResponse
import com.example.openweatherapp.remote.RetrofitInstance
import com.example.openweatherapp.remote.WeatherApi
import com.example.openweatherapp.repository.SettingsRepository
import com.example.openweatherapp.utils.ConnectivityObserver
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
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
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: SettingsRepository
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var weatherApi: WeatherApi
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        connectivityObserver = mockk(relaxed = true)
        weatherApi = mockk()

        mockkObject(RetrofitInstance)
        every { RetrofitInstance.weatherApi(any()) } returns weatherApi
        
        every { connectivityObserver.isConnected } returns flowOf(false)
        every { repository.lastCityFlow } returns flowOf("Loading...")
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `isOnline flow reflects connectivity observer status`() = runTest {
        val isConnectedFlow = MutableStateFlow(false)
        every { connectivityObserver.isConnected } returns isConnectedFlow
        
        viewModel = WeatherViewModel(repository, connectivityObserver)
        
        viewModel.isOnline.test {
            assertEquals(false, awaitItem())
            isConnectedFlow.value = true
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `getCityData updates result to Error when offline`() = runTest {
        every { connectivityObserver.isConnected } returns flowOf(false)
        viewModel = WeatherViewModel(repository, connectivityObserver)

        viewModel.getCityData("London")
        testDispatcher.scheduler.advanceUntilIdle()


        assertTrue(viewModel.locationResult.value is NetworkResponse.Error)
        assertEquals("No Internet Connection", (viewModel.locationResult.value as NetworkResponse.Error).message)
    }

    @Test
    fun `getCityData success triggers weather fetch`() = runTest {
        every { connectivityObserver.isConnected } returns flowOf(true)
        val locationItem = mockk<LocationModelItem>(relaxed = true) {
            every { lat } returns 51.5074
            every { lon } returns -0.1278
        }
        val locationModel = LocationModel().apply { add(locationItem) }
        val weatherModel = mockk<WeatherModel>(relaxed = true)

        coEvery { weatherApi.getCityLocation(eq(BuildConfig.API_KEY), "London") } returns Response.success(locationModel)
        coEvery { weatherApi.getCityWeather(eq(BuildConfig.API_KEY), 51.5074, -0.1278) } returns Response.success(weatherModel)

        viewModel = WeatherViewModel(repository, connectivityObserver)
        viewModel.getCityData("London")

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.locationResult.value is NetworkResponse.Success)
        assertTrue(viewModel.weatherResult.value is NetworkResponse.Success)
    }

    @Test
    fun `getCityData location API error updates state`() = runTest {
        every { connectivityObserver.isConnected } returns flowOf(true)
        coEvery { weatherApi.getCityLocation(eq(BuildConfig.API_KEY), any()) } returns Response.error(404, mockk(relaxed = true))

        viewModel = WeatherViewModel(repository, connectivityObserver)
        viewModel.getCityData("InvalidCity")

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.locationResult.value is NetworkResponse.Error)
    }

    @Test
    fun `getLatLongData weather API error updates state`() = runTest {
        every { connectivityObserver.isConnected } returns flowOf(true)
        coEvery { weatherApi.getCityWeather(eq(BuildConfig.API_KEY), any(), any()) } returns Response.error(500, mockk(relaxed = true))

        viewModel = WeatherViewModel(repository, connectivityObserver)
        viewModel.getLatLongData(0.0, 0.0)

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.weatherResult.value is NetworkResponse.Error)
    }

    @Test
    fun `updateCity calls repository saveCity`() = runTest {
        viewModel = WeatherViewModel(repository, connectivityObserver)
        viewModel.updateCity("New York")
        
        testDispatcher.scheduler.advanceUntilIdle()
        coroutineScope {
            coVerify { repository.saveCity("New York") }
        }

    }
}