package com.example.openweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweatherapp.data.WeatherModel
import com.example.openweatherapp.remote.NetworkResponse
import com.example.openweatherapp.repository.SettingsRepository
import com.example.openweatherapp.repository.WeatherRepository
import com.example.openweatherapp.utils.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val weatherRepository: WeatherRepository,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _weatherResult = MutableStateFlow<NetworkResponse<WeatherModel>>(NetworkResponse.NullCheck)
    val weatherResult: StateFlow<NetworkResponse<WeatherModel>> = _weatherResult

    val cityName: StateFlow<String> = settingsRepository.lastCityFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Loading..."
        )

    val isOnline: StateFlow<Boolean> = connectivityObserver.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    init {
        viewModelScope.launch {
            settingsRepository.lastCityFlow.collect { savedCity ->
                if (savedCity != "Loading..." && _weatherResult.value is NetworkResponse.NullCheck) {
                    getWeatherData(savedCity)
                }
            }
        }
    }

    fun getWeatherData(city: String) {
        updateCity(city)

        viewModelScope.launch {
            if (!isOnline.value) {
                _weatherResult.value = NetworkResponse.Error("No Internet Connection")
                return@launch
            }

            _weatherResult.value = NetworkResponse.Loading
            val response = weatherRepository.getWeather(city)
            _weatherResult.value = response
        }
    }

    private fun updateCity(newCity: String) {
        viewModelScope.launch {
            settingsRepository.saveCity(newCity)
        }
    }
}