package com.example.openweatherapp.data

data class LocationModelItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String,
    val state: String
)