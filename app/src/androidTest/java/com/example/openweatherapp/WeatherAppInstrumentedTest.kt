package com.example.openweatherapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.openweatherapp.ui.presentation.MainActivity
import org.junit.Rule
import org.junit.Test

class WeatherAppInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testSearchBarIsDisplayed() {
        composeTestRule.onNodeWithText("Search by City").assertIsDisplayed()
        
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
    }

    @Test
    fun testSearchFunctionality_withEmptyCity() {
        composeTestRule.onNodeWithContentDescription("Search").performClick()

        composeTestRule.onNodeWithText("Search by City").assertIsDisplayed()
    }

    @Test
    fun testSearchInput() {
        val cityName = "London"
        // Enter a city name
        composeTestRule.onNodeWithText("Search by City").performTextInput(cityName)
        
        // Verify text is entered
        composeTestRule.onNodeWithText(cityName).assertIsDisplayed()
        
        // Perform search
        composeTestRule.onNodeWithContentDescription("Search").performClick()
    }

}
