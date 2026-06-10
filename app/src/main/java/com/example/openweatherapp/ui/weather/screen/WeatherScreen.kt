package com.example.openweatherapp.ui.weather.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.openweatherapp.R
import com.example.openweatherapp.ui.theme.Dimens
import com.example.openweatherapp.ui.weather.WeatherIntent
import com.example.openweatherapp.ui.weather.WeatherState
import com.example.openweatherapp.viewmodel.WeatherViewModel

/**
 * The entry point for the Weather Screen.
 */
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    WeatherPage(
        modifier = modifier,
        viewModel = viewModel
    )
}

@Composable
internal fun WeatherPage(
    modifier: Modifier,
    viewModel: WeatherViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.PaddingLarge)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        
        SearchBar(
            viewModel = viewModel,
            state = state
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        AnimatedVisibility(
            visible = !state.isOnline,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ErrorMessage(message = stringResource(R.string.no_internet))
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading && state.weatherData == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(Dimens.PaddingLarge)
                            .semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }

                state.error != null && state.weatherData == null -> {
                    ErrorMessage(message = state.error!!)
                }

                state.weatherData != null -> {
                    Column {
                        if (state.isLoading) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dimens.PaddingSmall)
                                    .padding(bottom = Dimens.PaddingSmall)
                            )
                        }
                        WeatherDetails(
                            data = state.weatherData!!,
                            iconUrl = state.weatherIconUrl
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    viewModel: WeatherViewModel,
    state: WeatherState
) {
    var cityQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchResults = state.searchResults.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = cityQuery,
            onValueChange = {
                cityQuery = it
                if (it.length > 2) {
                    viewModel.onIntent(WeatherIntent.SearchCities(it))
                }
            },
            label = { Text(text = stringResource(R.string.search_hint)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                if (cityQuery.isNotEmpty()) {
                    IconButton(onClick = { 
                        cityQuery = ""
                        viewModel.onIntent(WeatherIntent.ClearSearchResults)
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(Dimens.PaddingLarge),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            )
        )

        if (searchResults.itemCount > 0 && cityQuery.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.PaddingSmall)
                    .heightIn(max = Dimens.SearchResultsMaxHeight),
                elevation = CardDefaults.cardElevation(Dimens.CardElevation),
                shape = RoundedCornerShape(Dimens.PaddingMedium)
            ) {
                LazyColumn {
                    items(searchResults.itemCount) { index ->
                        val item = searchResults[index]
                        item?.let {
                            CitySearchResultItem(it) {
                                viewModel.onIntent(WeatherIntent.FetchWeather(it.name))
                                viewModel.onIntent(WeatherIntent.ClearSearchResults)
                                cityQuery = it.name
                                keyboardController?.hide()
                            }
                        }
                    }
                }
            }
        }
    }
}
