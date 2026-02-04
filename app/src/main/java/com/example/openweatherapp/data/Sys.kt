package com.example.openweatherapp.data

data class Sys(
    val country: String,
    val id: Int,
    val message: String,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)