# 🌦️ OpenWeather App

A modern, clean, and accessible Android Weather application built with **Jetpack Compose** and the **OpenWeather API**. This app demonstrates best practices in Android development, including MVVM architecture, clean code principles, and accessibility (ADA) optimizations.

## ✨ Features

- **Real-time Weather**: Fetch current weather data for any city globally.
- **Dynamic City Search**: Intelligent search with debounced queries using Paging 3 for a smooth experience.
- **Detailed Metrics**: View temperature (min/max), humidity, wind speed, pressure, and feels-like temperature.
- **Solar Cycle**: Accurate sunrise and sunset times formatted for your local timezone.
- **Modern UI**: Material 3 design with color-coded iconography and responsive layouts.
- **Robust Networking**: Handles offline states and API errors gracefully.

## 🛠️ Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for a declarative UI.
- **Architecture**: MVVM with a focus on Clean Architecture (Use Cases, Repositories).
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for predictable scoping.
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson).
- **Async & Streams**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and [Flow](https://kotlinlang.org/docs/flow.html).
- **Pagination**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data) for efficient city search results.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) for asynchronous weather icon loading.
- **Storage**: [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) for persisting the last searched city.

## ♿ Accessibility (ADA Optimization)

This app is designed to be inclusive:
- **Semantic Labels**: Custom `contentDescription` for all weather data and icons.
- **LiveRegions**: Automatic announcements for error messages and loading states for Screen Reader (TalkBack) users.
- **Touch Targets**: Large, accessible interactive elements meeting Material Design standards.
- **High Contrast**: Carefully selected color palettes for readability.

## 🚀 Setup & Installation

### 1. Get an API Key
Sign up at [OpenWeatherMap](https://openweathermap.org/api) to get your free API key.

### 2. Configure the App
The project uses the `Secrets Gradle Plugin`. While a default key is provided in `secrets.defaults.properties`, you should use your own for development:

1. Create a `local.properties` file in the root directory (if it doesn't exist).
2. Add your API key:
   ```properties
   API_KEY=your_actual_api_key_here
   ```

### 3. Build & Run
Open the project in **Android Studio (Ladybug or newer)** and run the `:app` module.

## 🧪 Testing

The project includes a comprehensive test suite using **JUnit 4**, **MockK**, **Turbine**, and **Robolectric**:
- **Unit Tests**: Business logic and Use Cases.
- **ViewModel Tests**: State management and Intent handling.
- **UI Tests**: Component-level verification with Compose Test Rule.

Run tests via terminal:
```bash
./gradlew test
```

## 📂 Project Structure

- `data/`: Remote API interfaces, DTOs, and Repository implementations.
- `domain/`: Business logic, Use Cases, and Repository interfaces.
- `ui/`: Compose screens, components, themes, and UI logic (State/Intent).
- `di/`: Hilt modules for dependency management.
- `utils/`: Connectivity observers and resource providers.
