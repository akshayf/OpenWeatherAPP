package com.example.openweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.openweatherapp.domain.usecase.GetLastCityUseCase
import com.example.openweatherapp.domain.usecase.GetWeatherUseCase
import com.example.openweatherapp.domain.usecase.SaveLastCityUseCase
import com.example.openweatherapp.domain.usecase.SearchCitiesUseCase
import com.example.openweatherapp.BuildConfig
import com.example.openweatherapp.data.remote.NetworkResponse
import com.example.openweatherapp.ui.weather.WeatherIntent
import com.example.openweatherapp.ui.weather.WeatherState
import com.example.openweatherapp.utils.ConnectivityObserver
import com.example.openweatherapp.utils.ResourceProvider
import com.example.openweatherapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getLastCityUseCase: GetLastCityUseCase,
    private val saveLastCityUseCase: SaveLastCityUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val connectivityObserver: ConnectivityObserver,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    init {
        observeCityName()
        observeConnectivity()
        observeSearchQuery()
    }

    /**
     * Handles user interactions and dispatches state updates.
     */
    fun onIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.FetchWeather -> fetchWeatherData(intent.city)
            is WeatherIntent.RefreshWeather -> fetchWeatherData(intent.city)
            is WeatherIntent.SearchCities -> searchQuery.value = intent.query
            WeatherIntent.ClearSearchResults -> clearSearchResults()
        }
    }

    /**
     * Clears city search results and search query.
     */
    private fun clearSearchResults() {
        searchQuery.value = ""
        _state.update { it.copy(searchResults = emptyFlow()) }
    }

    /**
     * Observes the search query flow with debounce to limit API calls.
     */
    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(300L)
                .distinctUntilChanged()
                .filter { it.length > 2 }
                .collect { query ->
                    searchCities(query)
                }
        }
    }

    /**
     * Observes the last searched city from settings and triggers initial fetch.
     */
    private fun observeCityName() {
        viewModelScope.launch {
            getLastCityUseCase().collect { city ->
                _state.update { it.copy(cityName = city) }
                if (_state.value.weatherData == null) {
                    fetchWeatherData(city)
                }
            }
        }
    }

    /**
     * Observes network connectivity status.
     */
    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.isConnected.collect { isConnected ->
                _state.update { it.copy(isOnline = isConnected) }
            }
        }
    }

    /**
     * Searches for cities matching the query using Paging 3.
     */
    private fun searchCities(query: String) {
        val results = searchCitiesUseCase(query).cachedIn(viewModelScope)
        _state.update { it.copy(searchResults = results) }
    }

    /**
     * Fetches weather data for a specific city.
     */
    private fun fetchWeatherData(city: String) {
        viewModelScope.launch {
            if (!_state.value.isOnline) {
                _state.update { it.copy(error = resourceProvider.getString(R.string.no_internet), isLoading = false) }
                return@launch
            }

            _state.update { it.copy(isLoading = true, error = null) }
            
            saveLastCityUseCase(city)

            when (val response = getWeatherUseCase(city)) {
                is NetworkResponse.Success -> {
                    val iconCode = response.data.weather.firstOrNull()?.icon
                    val iconUrl = iconCode?.let { 
                        resourceProvider.getString(R.string.icon_url_format, BuildConfig.ICON_URL, it)
                    }
                    _state.update { 
                        it.copy(
                            weatherData = response.data,
                            weatherIconUrl = iconUrl,
                            isLoading = false,
                            error = null
                        ) 
                    }
                }
                is NetworkResponse.Error -> {
                    _state.update { 
                        it.copy(
                            error = response.message,
                            isLoading = false
                        ) 
                    }
                }
            }
        }
    }
}
