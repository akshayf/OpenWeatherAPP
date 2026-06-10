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

/**
 * Instrumented tests for the Weather App UI.
 * Optimizations:
 * - Uses string resources for better maintainability and localization support.
 * - Improved synchronization by verifying UI state transitions.
 */
class WeatherAppInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val searchHint by lazy { composeTestRule.activity.getString(R.string.search_hint) }
    private val searchDesc by lazy { composeTestRule.activity.getString(R.string.search_desc) }

    @Test
    fun testSearchBarIsDisplayed() {
        composeTestRule.onNodeWithText(searchHint).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(searchDesc).assertIsDisplayed()
    }

    @Test
    fun testSearchFunctionality_withEmptyCity() {
        // Clicking search with empty input shouldn't change the base state
        composeTestRule.onNodeWithContentDescription(searchDesc).performClick()
        composeTestRule.onNodeWithText(searchHint).assertIsDisplayed()
    }

    @Test
    fun testSearchInput() {
        val cityName = "London"
        
        // Enter a city name
        composeTestRule.onNodeWithText(searchHint).performTextInput(cityName)
        
        // Verify text is entered
        composeTestRule.onNodeWithText(cityName).assertIsDisplayed()
        
        // Perform search
        composeTestRule.onNodeWithContentDescription(searchDesc).performClick()
        
        // Verification: Since this is an integration test, we verify the dropdown or result appearance
        // Note: Real network calls might delay this, but ComposeTestRule handles idling.
        val selectCityLabel = composeTestRule.activity.getString(R.string.select_city, cityName)
        composeTestRule.onNodeWithContentDescription(selectCityLabel).assertIsDisplayed()
    }
}
