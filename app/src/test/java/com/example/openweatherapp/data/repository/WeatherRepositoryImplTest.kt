package com.example.openweatherapp.data.repository

import com.example.openweatherapp.data.remote.NetworkResponse
import com.example.openweatherapp.data.remote.api.WeatherApi
import com.example.openweatherapp.data.remote.dto.LocationModel
import com.example.openweatherapp.data.remote.dto.LocationModelItem
import com.example.openweatherapp.data.remote.dto.WeatherModel
import com.example.openweatherapp.utils.ResourceProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherRepositoryImplTest {

    private lateinit var weatherApi: WeatherApi
    private lateinit var locationApi: WeatherApi
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        weatherApi = mockk()
        locationApi = mockk()
        resourceProvider = mockk()
        repository = WeatherRepositoryImpl(weatherApi, resourceProvider, locationApi)
    }

    @Test
    fun `getWeather success returns Success response`() = runTest {
        val cityName = "London"
        val lat = 51.5074
        val lon = -0.1278
        
        val locationItem = mockk<LocationModelItem>(relaxed = true) {
            coEvery { this@mockk.lat } returns lat
            coEvery { this@mockk.lon } returns lon
        }
        val locationModel = LocationModel().apply { add(locationItem) }
        val weatherModel = mockk<WeatherModel>(relaxed = true)

        coEvery { locationApi.getCityLocation(cityName) } returns Response.success(locationModel)
        coEvery { weatherApi.getCityWeather(lat, lon) } returns Response.success(weatherModel)

        val result = repository.getWeather(cityName)

        assertTrue(result is NetworkResponse.Success)
        assertEquals(weatherModel, (result as NetworkResponse.Success).data)
    }

    @Test
    fun `getWeather city not found returns Error`() = runTest {
        val cityName = "Unknown"
        coEvery { locationApi.getCityLocation(cityName) } returns Response.success(LocationModel())
        every { resourceProvider.getString(any()) } returns "City not found"

        val result = repository.getWeather(cityName)

        assertTrue(result is NetworkResponse.Error)
        assertEquals("City not found", (result as NetworkResponse.Error).message)
    }
}
