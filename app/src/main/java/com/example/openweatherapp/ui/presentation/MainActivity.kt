package com.example.openweatherapp.ui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.openweatherapp.BuildConfig
import com.example.openweatherapp.R
import com.example.openweatherapp.data.WeatherModel
import com.example.openweatherapp.model.WeatherViewModel
import com.example.openweatherapp.remote.NetworkResponse
import com.example.openweatherapp.ui.theme.OpenWeatherAPPTheme
import com.example.openweatherapp.utils.LoggerUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weatherViewModel : WeatherViewModel by viewModels()

        enableEdgeToEdge()
        setContent {
            OpenWeatherAPPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherPage(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        weatherViewModel
                    )
                }
            }
        }
    }
}

/**
 * Home page with initial search option for users
 */
@Composable
fun WeatherPage(name: String, modifier: Modifier, viewModel: WeatherViewModel) {

    var city by remember { mutableStateOf("") }

    val weatherResult by viewModel.weatherResult.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current

    LoggerUtil.debug("weatherResult $weatherResult")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 20.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp, start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = {
                    city = it
                },
                label = { Text(text = "Search by City") }
            )

            IconButton(modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically),
                onClick = { viewModel.getCityData(city.trim())
                    keyboardController?.hide()}) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                )
            }
        }

        if (!isOnline) {
            Text(
                text = "No Internet Connection",
                color = Color.White,
                modifier = Modifier.background(Color.Red).fillMaxWidth()
                    .padding(all = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            //Internet is available
        }

        when (val result = weatherResult) {
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data, Modifier)

            }

            is NetworkResponse.Error -> {
                Text(
                    text = result.message,
                    color = Color.White,
                    modifier = Modifier.background(Color.Red).fillMaxWidth()
                        .padding(all = 10.dp)
                        .align(Alignment.CenterHorizontally)
                )

            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.NullCheck -> {}
        }
    }
}

/**
 * Show weather details as per API response
 */
@Composable
fun WeatherDetails(data: WeatherModel, modifier: Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.name, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.sys.country, fontSize = 17.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "${data.main.temp}°C", fontSize = 55.sp)

        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = BuildConfig.ICON_URL + data.weather[0].icon + "@2x.png",
            contentDescription = "Weather Icon",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.error)
        )

        Text(text = data.weather[0].description, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)

        Spacer(modifier = Modifier.height(20.dp))

        Card {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue(value = "${data.main.temp_min.toString()}°C", key = "Temp Min")
                    WeatherKeyValue(value = "${data.main.temp_max.toString()}°C", key = "Temp Max")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue(value = data.main.humidity, key = "Humidity")
                    WeatherKeyValue(value = data.main.pressure.toString(), key = "Pressure")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue(value = data.wind.speed.toString(), key = "Wind Speed")
                    WeatherKeyValue(value = data.main.feels_like.toString(), key = "Feels Like")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue(value = data.coord.lat.toString(), key = "Lat")
                    WeatherKeyValue(value = data.coord.lon.toString(), key = "Long")
                }
            }
        }


    }
}


@Composable
fun WeatherKeyValue(key: String, value: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}
