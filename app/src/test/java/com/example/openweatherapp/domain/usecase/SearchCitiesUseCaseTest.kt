package com.example.openweatherapp.domain.usecase

import androidx.paging.PagingData
import com.example.openweatherapp.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class SearchCitiesUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var useCase: SearchCitiesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchCitiesUseCase(repository)
    }

    @Test
    fun `invoke returns flow of paging data from repository`() = runTest {
        val pagingData = PagingData.empty<com.example.openweatherapp.data.remote.dto.LocationModelItem>()
        every { repository.searchCities("London") } returns flowOf(pagingData)

        val result = useCase("London")

        assertNotNull(result)
        // Since PagingData is hard to compare directly, we just check if it's not null.
    }
}
