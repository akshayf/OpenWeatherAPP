package com.example.openweatherapp.data.remote

/**
 * Sealed class representing the result of a network request.
 */
sealed class NetworkResponse<out T> {
    /** Represents a successful response containing data. */
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    
    /** Represents an error response with a message. */
    data class Error(val message: String) : NetworkResponse<Nothing>()
}
