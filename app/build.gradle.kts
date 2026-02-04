plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.openweatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.openweatherapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "WEATHER_URL", "\"https://api.openweathermap.org/data/2.5/\"")
            buildConfigField("String", "LOCATION_URL", "\"https://api.openweathermap.org/geo/1.0/\"")
            buildConfigField("String", "ICON_URL", "\"https://openweathermap.org/img/wn/\"")
            buildConfigField("String", "API_KEY", "\"28c17cf9201e7ff9acd7bb11b03333ed\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "WEATHER_URL", "\"https://api.openweathermap.org/data/2.5/\"")
            buildConfigField("String", "LOCATION_URL", "\"https://api.openweathermap.org/geo/1.0/\"")
            buildConfigField("String", "ICON_URL", "\"https://openweathermap.org/img/wn/\"")
            buildConfigField("String", "API_KEY", "\"28c17cf9201e7ff9acd7bb11b03333ed\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.play.services.contextmanager) { exclude(group = "com.android.support", module = "support-v4") }
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //Retrofit dependencies
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    //Coil Compose dependency
    implementation(libs.coil.compose)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.compiler)


    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Coroutines test
    testImplementation(libs.kotlinx.coroutines.test)

    // Mockk
    testImplementation(libs.mockk)

    // Turbine
    testImplementation(libs.turbine)
}
