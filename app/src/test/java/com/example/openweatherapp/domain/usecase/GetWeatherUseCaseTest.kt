package com.example.openweatherapp.domain.usecase

import com.example.openweatherapp.data.remote.NetworkResponse
import com.example.openweatherapp.data.remote.dto.WeatherModel
import com.example.openweatherapp.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetWeatherUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var getWeatherUseCase: GetWeatherUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getWeatherUseCase = GetWeatherUseCase(repository)
    }

    @Test
    fun `invoke should return weather from repository`() = runTest {
        val weatherModel = mockk<WeatherModel>()
        val expectedResponse = NetworkResponse.Success(weatherModel)
        coEvery { repository.getWeather("London") } returns expectedResponse

        val result = getWeatherUseCase("London")

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `invoke should return error from repository when failed`() = runTest {
        val expectedResponse = NetworkResponse.Error("City not found")
        coEvery { repository.getWeather("Unknown") } returns expectedResponse

        val result = getWeatherUseCase("Unknown")

        assertEquals(expectedResponse, result)
    }
}
