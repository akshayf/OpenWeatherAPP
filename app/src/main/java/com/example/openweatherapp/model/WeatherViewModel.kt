package com.example.openweatherapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweatherapp.BuildConfig
import com.example.openweatherapp.data.LocationModel
import com.example.openweatherapp.data.WeatherModel
import com.example.openweatherapp.remote.NetworkResponse
import com.example.openweatherapp.remote.RetrofitInstance
import com.example.openweatherapp.repository.SettingsRepository
import com.example.openweatherapp.utils.ConnectivityObserver
import com.example.openweatherapp.utils.LoggerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: SettingsRepository,
                                           connectivityObserver: ConnectivityObserver) : ViewModel() {

    private val locationApi = RetrofitInstance.weatherApi(BuildConfig.LOCATION_URL)
    private val weatherApi = RetrofitInstance.weatherApi(BuildConfig.WEATHER_URL)

    private val _locationResult = MutableStateFlow<NetworkResponse<LocationModel>>(NetworkResponse.NullCheck)
    val locationResult: StateFlow<NetworkResponse<LocationModel>> = _locationResult

    private val _weatherResult = MutableStateFlow<NetworkResponse<WeatherModel>>(NetworkResponse.NullCheck)
    val weatherResult: StateFlow<NetworkResponse<WeatherModel>> = _weatherResult

    /**
     * Observe the last searched city
     */
    val cityName: StateFlow<String> = repository.lastCityFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Loading..."
        )

    /**
     * Observe connectivity status
     */
    val isOnline: StateFlow<Boolean> = connectivityObserver.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    init {
        viewModelScope.launch {
            // Collect the first non-loading value to trigger initial load
            repository.lastCityFlow.collect { savedCity ->
                if (savedCity != "Loading..." && _weatherResult.value is NetworkResponse.NullCheck) {
                    getCityData(savedCity)
                }
            }
        }
    }

    /**
     * API call to get lat and long of the city
     * @param city
     */
    fun getCityData(city: String) {
        LoggerUtil.debug("city $city")
        updateCity(city)
        
        if (!isOnline.value) {
            _locationResult.value = NetworkResponse.Error("No Internet Connection")
            return
        }

        _locationResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = locationApi.getCityLocation(BuildConfig.API_KEY, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _locationResult.value = NetworkResponse.Success(it)
                        getLatLongData(it[0].lat, it[0].lon)
                    }
                } else {
                    _locationResult.value = NetworkResponse.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _locationResult.value = NetworkResponse.Error("Error: ${e.message}")
            }
        }
    }

    /**
     * API call to get weather details of the lat and long
     * @param lat
     * @param lon
     */
    fun getLatLongData(lat: Double, lon: Double) {
        if (!isOnline.value) {
            _weatherResult.value = NetworkResponse.Error("No Internet Connection")
            return
        }

        _weatherResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = weatherApi.getCityWeather(BuildConfig.API_KEY, lat, lon)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Error: ${e.message}")
            }
        }
    }

    /**
     * Update the last searched city
     */
    fun updateCity(newCity: String) {
        viewModelScope.launch {
            repository.saveCity(newCity)
        }
    }
}
