package com.example.openweatherapp.ui.weather.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.openweatherapp.viewmodel.WeatherViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.openweatherapp.ui.weather.WeatherState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WeatherScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WeatherScreen shows search hint`() {
        val viewModel = mockk<WeatherViewModel>(relaxed = true)
        every { viewModel.state } returns MutableStateFlow(WeatherState())

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Search by City").assertExists()
    }
}
