package com.example.openweatherapp.data.remote.interceptor

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {

    private lateinit var authInterceptor: AuthInterceptor
    private lateinit var chain: Interceptor.Chain

    @Before
    fun setUp() {
        authInterceptor = AuthInterceptor()
        chain = mockk(relaxed = true)
    }

    @Test
    fun `intercept appends appid query parameter`() {
        val originalRequest = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=London")
            .build()
        
        every { chain.request() } returns originalRequest

        authInterceptor.intercept(chain)

        verify {
            chain.proceed(withArg {
                assert(it.url.queryParameter("appid") != null)
            })
        }
    }
}
