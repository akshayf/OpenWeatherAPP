# 🌦️ OpenWeather App

I built this app to be a clean, modern example of how a weather app should look and feel on Android today. It’s more than just a weather fetcher—it’s designed with a real focus on accessibility (ADA) and clean architecture using **Jetpack Compose**.

## 🚀 What this app does

*   **Smart Search**: Just start typing a city. It uses Paging 3 and debouncing so it won't spam the API while you're still typing.
*   **Weather at a Glance**: You get the main temperature front and center, with a nice color-coded grid for the details like humidity, wind, and pressure.
*   **Daylight Cycle**: I added sunrise and sunset times (formatted to your local time) because knowing when the sun is up is just as important as the temperature.
*   **Offline Ready**: If you lose your connection, the app won't just crash or show a blank screen; it'll let you know what's going on.

## 🧠 How it was built (The "under the hood" stuff)

I wanted to follow modern Android best practices as closely as possible:

*   **Architecture**: It uses **MVVM** with a clear separation of concerns. I used **Use Cases** to handle the business logic so the ViewModels stay lean.
*   **UI**: 100% **Jetpack Compose**. It’s declarative, fast, and makes handling UI states way easier.
*   **Dependency Injection**: Powered by **Hilt** to keep everything decoupled and testable.
*   **Image Loading**: Used **Coil** for the weather icons because it’s lightweight and plays nice with Compose.

## ♿ Why Accessibility Matters

One thing I'm proud of is the accessibility work. I've added custom semantic properties and `LiveRegions`. This means if you're using TalkBack, the app will actually announce errors or changes as they happen, rather than leaving the user guessing.

## 🛠️ Setting it up locally

Since we don't want to commit private API keys to Git, I've set up a system using the `Secrets Gradle Plugin`.

1.  **Get your key**: Head over to [OpenWeatherMap](https://openweathermap.org/api) and grab a free API key.
2.  **Add your key**: You have two options:
    *   **Quick start**: I've included a `secrets.defaults.properties` file where you can drop your key.
    *   **Better way**: Create a `local.properties` file in your root folder and add `API_KEY=your_key_here`. This is the safest way as it's ignored by Git.
3.  **Build**: Open it in Android Studio (Ladybug+) and you're good to go.

## 🧪 Testing

I believe in code that actually works. There are unit tests for the logic and ViewModels, plus UI tests for the Compose components using **MockK** and **Robolectric**. You can run them all with:
```bash
./gradlew test
```

---
*Feel free to explore the code! I've tried to keep the package structure very organized (`data`, `domain`, `ui`) so it's easy to navigate.*
