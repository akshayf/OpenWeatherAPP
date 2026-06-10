package com.example.openweatherapp.data.remote.paging

import androidx.paging.PagingSource
import com.example.openweatherapp.data.remote.api.WeatherApi
import com.example.openweatherapp.data.remote.dto.LocationModel
import com.example.openweatherapp.data.remote.dto.LocationModelItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CityPagingSourceTest {

    private lateinit var weatherApi: WeatherApi
    private lateinit var cityPagingSource: CityPagingSource

    @Before
    fun setUp() {
        weatherApi = mockk()
    }

    @Test
    fun `load returns success on valid response`() = runTest {
        val cities = LocationModel().apply { add(mockk<LocationModelItem>(relaxed = true)) }
        coEvery { weatherApi.getCityLocation("London", any()) } returns Response.success(cities)

        cityPagingSource = CityPagingSource(weatherApi, "London")

        val expectedResult = PagingSource.LoadResult.Page(
            data = cities,
            prevKey = null,
            nextKey = null
        )

        assertEquals(
            expectedResult,
            cityPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `load returns error on API failure`() = runTest {
        val exception = RuntimeException("Network Error")
        coEvery { weatherApi.getCityLocation(any(), any()) } throws exception

        cityPagingSource = CityPagingSource(weatherApi, "London")

        val result = cityPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(exception, (result as PagingSource.LoadResult.Error).throwable)
    }
}
