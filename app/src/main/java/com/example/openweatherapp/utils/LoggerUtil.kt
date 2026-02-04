package com.example.openweatherapp.utils

import com.example.openweatherapp.BuildConfig

/**
 * Custom logger for the app
 */
object LoggerUtil {

    private val isDebug = BuildConfig.DEBUG
    const val TAG = "OpenWeatherApp"


    fun debug(message: String) {
        if (isDebug) {
            android.util.Log.d(TAG, message)
        }
    }

    fun error(message: String, throwable: Throwable? = null) {
        if (isDebug) {
            if (throwable != null) {
                android.util.Log.e(TAG, message, throwable)
            } else {
                android.util.Log.e(TAG, message)
            }
        }
    }

}