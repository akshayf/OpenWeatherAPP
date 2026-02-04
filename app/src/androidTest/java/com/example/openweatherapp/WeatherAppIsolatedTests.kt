package com.example.openweatherapp

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.openweatherapp.data.Clouds
import com.example.openweatherapp.data.Coord
import com.example.openweatherapp.data.Main
import com.example.openweatherapp.data.Sys
import com.example.openweatherapp.data.Weather
import com.example.openweatherapp.data.WeatherModel
import com.example.openweatherapp.data.Wind
import com.example.openweatherapp.ui.presentation.LocationPermissionDialog
import com.example.openweatherapp.ui.presentation.WeatherDetails
import com.example.openweatherapp.ui.theme.OpenWeatherAPPTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class WeatherAppIsolatedTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testWeatherDetailsDisplay() {
        // Mocking WeatherModel data
        val mockWeather = WeatherModel(
            base = "stations",
            clouds = Clouds(all = 0),
            cod = 200,
            coord = Coord(lat = 51.5074, lon = -0.1278),
            dt = 1618317040,
            id = 2643743,
            main = Main(
                feels_like = 10.0,
                humidity = "50",
                pressure = 1012,
                temp = 15.0,
                temp_max = 18.0,
                temp_min = 12.0
            ),
            name = "London",
            sys = Sys(
                country = "GB", id = 1, sunrise = 1618292034, sunset = 1618340710, type = 1,
                message = "",
            ),
            visibility = 10000,
            weather = listOf(Weather(description = "clear sky", icon = "01d", id = 800, main = "Clear")),
            wind = Wind(deg = 0, speed = 5.0)
        )

        composeTestRule.setContent {
            OpenWeatherAPPTheme {
                WeatherDetails(data = mockWeather, modifier = Modifier)
            }
        }

        // Verify city name and temperature are displayed
        composeTestRule.onNodeWithText("London").assertIsDisplayed()
        composeTestRule.onNodeWithText("15.0°C").assertIsDisplayed()

        // Verify detail labels are present
        composeTestRule.onNodeWithText("Humidity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Wind Speed").assertIsDisplayed()
        composeTestRule.onNodeWithText("Feels Like").assertIsDisplayed()
    }

    @Test
    fun testLocationPermissionDialog_confirm() {
        var confirmClicked = false
        var dismissClicked = false

        composeTestRule.setContent {
            OpenWeatherAPPTheme {
                LocationPermissionDialog(
                    onConfirm = { confirmClicked = true },
                    onDismiss = { dismissClicked = true }
                )
            }
        }

        // Test Allow button
        composeTestRule.onNodeWithText("Allow").performClick()
        assertTrue(confirmClicked)
        assertFalse(dismissClicked)
    }

    @Test
    fun testLocationPermissionDialog_dismiss() {
        var confirmClicked = false
        var dismissClicked = false

        composeTestRule.setContent {
            OpenWeatherAPPTheme {
                LocationPermissionDialog(
                    onConfirm = { confirmClicked = true },
                    onDismiss = { dismissClicked = true }
                )
            }
        }

        // Test Not Now button
        composeTestRule.onNodeWithText("Not Now").performClick()
        assertTrue(dismissClicked)
        assertFalse(confirmClicked)
    }
}