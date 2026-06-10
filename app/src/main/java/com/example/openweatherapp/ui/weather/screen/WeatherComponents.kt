package com.example.openweatherapp.ui.weather.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import coil.compose.AsyncImage
import com.example.openweatherapp.R
import com.example.openweatherapp.data.remote.dto.LocationModelItem
import com.example.openweatherapp.data.remote.dto.WeatherModel
import com.example.openweatherapp.ui.theme.Dimens

/**
 * Optimized for ADA (Accessibility):
 * - Uses LiveRegion to announce errors.
 * - Groups related info for screen readers.
 * - Minimum touch target sizes.
 */

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.onError,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.error)
            .fillMaxWidth()
            .padding(Dimens.PaddingNormal)
            .semantics { 
                liveRegion = LiveRegionMode.Assertive 
            } // TalkBack will announce this immediately
    )
}

@Composable
fun CitySearchResultItem(item: LocationModelItem, onClick: () -> Unit) {
    val description = remember(item) {
        buildString {
            append(item.name)
            if (!item.state.isNullOrEmpty()) {
                append(", ${item.state}")
            }
            append(" ${item.country}")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                onClickLabel = stringResource(R.string.select_city, item.name),
                role = Role.Button
            )
            .padding(Dimens.PaddingLarge)
            .semantics {
                contentDescription = description
            }
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )
        HorizontalDivider(modifier = Modifier.padding(top = Dimens.PaddingSmall))
    }
}

@Composable
fun WeatherDetails(
    data: WeatherModel,
    iconUrl: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherOverview(data, iconUrl)
        Spacer(modifier = Modifier.height(Dimens.VerticalSpacerLarge))
        WeatherStatsGrid(data)
    }
}

@Composable
fun WeatherOverview(data: WeatherModel, iconUrl: String?) {
    val locationDescription = stringResource(R.string.location_desc, data.name, data.sys.country)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = locationDescription
                heading()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(Dimens.PaddingExtraLarge),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(Dimens.PaddingSmall))
            Text(
                text = data.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(Dimens.PaddingSmall))
            Text(
                text = data.sys.country,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        val temperatureDescription = stringResource(R.string.temperature_desc, data.main.temp)
        Text(
            text = "${data.main.temp}°C",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics {
                contentDescription = temperatureDescription
            }
        )

        AsyncImage(
            modifier = Modifier.size(Dimens.WeatherIconSize),
            model = iconUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.error)
        )

        val weatherCondition = data.weather[0].description
        Text(
            text = weatherCondition.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.semantics { heading() }
        )
    }
}

@Composable
fun WeatherStatsGrid(data: WeatherModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingSmall)
            .semantics { 
                contentDescription = "Detailed weather statistics" 
            },
        shape = RoundedCornerShape(Dimens.PaddingLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingNormal)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                WeatherKeyValue(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.KeyboardArrowDown,
                    iconColor = Color(0xFF42A5F5), // Blue
                    key = stringResource(R.string.temp_min),
                    value = "${data.main.temp_min}°C"
                )
                WeatherKeyValue(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.KeyboardArrowUp,
                    iconColor = Color(0xFFEF5350), // Red
                    key = stringResource(R.string.temp_max),
                    value = "${data.main.temp_max}°C"
                )
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimens.PaddingLarge), color = MaterialTheme.colorScheme.outlineVariant)
            Row(modifier = Modifier.fillMaxWidth()) {
                WeatherKeyValue(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.WaterDrop,
                    iconColor = Color(0xFF26C6DA), // Cyan
                    key = stringResource(R.string.humidity),
                    value = "${data.main.humidity}%"
                )
                WeatherKeyValue(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Speed,
                    iconColor = Color(0xFF78909C), // Blue Grey
                    key = stringResource(R.string.pressure),
                    value = "${data.main.pressure} hPa"
                )
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimens.PaddingLarge), color = MaterialTheme.colorScheme.outlineVariant)
            Row(modifier = Modifier.fillMaxWidth()) {
                WeatherKeyValue(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Air,
                    iconColor = Color(0xFF66BB6A), // Green
                    key = stringResource(R.string.wind_speed),
                    value = "${data.wind.speed} m/s"
                )
                WeatherKeyValue(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Thermostat,
                    iconColor = Color(0xFFFFA726), // Orange
                    key = stringResource(R.string.feels_like),
                    value = "${data.main.feels_like}°C"
                )
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimens.PaddingLarge), color = MaterialTheme.colorScheme.outlineVariant)
            Row(modifier = Modifier.fillMaxWidth()) {
                WeatherKeyValue(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.WbSunny,
                    iconColor = Color(0xFFFFD600), // Yellow
                    key = stringResource(R.string.sunrise),
                    value = formatTime(data.sys.sunrise)
                )
                WeatherKeyValue(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.WbTwilight,
                    iconColor = Color(0xFFFF7043), // Deep Orange
                    key = stringResource(R.string.sunset),
                    value = formatTime(data.sys.sunset)
                )
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimens.PaddingLarge), color = MaterialTheme.colorScheme.outlineVariant)
            Row(modifier = Modifier.fillMaxWidth()) {
                WeatherKeyValue(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Explore,
                    iconColor = Color(0xFFAB47BC), // Purple
                    key = stringResource(R.string.lat),
                    value = data.coord.lat.toString()
                )
                WeatherKeyValue(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Explore,
                    iconColor = Color(0xFFAB47BC), // Purple
                    key = stringResource(R.string.lon),
                    value = data.coord.lon.toString()
                )
            }
        }
    }
}

private fun formatTime(unixTimestamp: Int): String {
    return try {
        val date = Date(unixTimestamp.toLong() * 1000)
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.format(date)
    } catch (e: Exception) {
        "--:--"
    }
}

@Composable
fun WeatherKeyValue(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconColor: Color,
    key: String,
    value: String
) {
    val description = stringResource(R.string.key_value_semantics, key, value)
    Column(
        modifier = modifier
            .padding(Dimens.PaddingMedium)
            .semantics(mergeDescendants = true) {
                contentDescription = description
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = key,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
