package com.example.openweatherapp.domain.usecase

import androidx.paging.PagingData
import com.example.openweatherapp.data.remote.dto.LocationModelItem
import com.example.openweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to search for cities matching a query string.
 */
class SearchCitiesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(query: String): Flow<PagingData<LocationModelItem>> {
        return repository.searchCities(query)
    }
}
