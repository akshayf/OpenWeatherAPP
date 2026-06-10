package com.example.openweatherapp.data.remote.interceptor

import com.example.openweatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor that appends the API Key to every outgoing request as a query parameter.
 */
@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {

    private val QUERY_APP_ID = "appid"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter(QUERY_APP_ID, BuildConfig.API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
