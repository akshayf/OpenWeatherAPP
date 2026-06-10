package com.example.openweatherapp.ui.weather.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.openweatherapp.data.remote.dto.LocationModelItem
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WeatherComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `ErrorMessage displays correct text`() {
        val errorMessage = "Something went wrong"
        composeTestRule.setContent {
            ErrorMessage(message = errorMessage)
        }
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun `CitySearchResultItem displays city details with state`() {
        val item = LocationModelItem(
            name = "London",
            state = "England",
            country = "GB",
            lat = 51.5074,
            lon = -0.1278,
            local_names = null
        )
        composeTestRule.setContent {
            CitySearchResultItem(item = item, onClick = {})
        }
        composeTestRule.onNodeWithText("London, England GB").assertExists()
    }

    @Test
    fun `CitySearchResultItem displays city details without state`() {
        val item = LocationModelItem(
            name = "London",
            state = null,
            country = "GB",
            lat = 51.5074,
            lon = -0.1278,
            local_names = null
        )
        composeTestRule.setContent {
            CitySearchResultItem(item = item, onClick = {})
        }
        composeTestRule.onNodeWithText("London GB").assertExists()
    }
}
