package com.example.openweatherapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.openweatherapp.data.remote.api.WeatherApi
import com.example.openweatherapp.data.remote.dto.LocationModelItem

/**
 * PagingSource for city search results using OpenWeather Geocoding API.
 */
class CityPagingSource(
    private val weatherApi: WeatherApi,
    private val query: String
) : PagingSource<Int, LocationModelItem>() {

    /**
     * Loads a page of city results.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationModelItem> {
        val position = params.key ?: 1
        return try {
            val response = weatherApi.getCityLocation(query, limit = 10)
            val cities = if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
            
            LoadResult.Page(
                data = cities,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (cities.isEmpty()) null else null // API doesn't support offset, so we stop after first page
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LocationModelItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
