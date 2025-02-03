plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.kaerusworld.newsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kaerusworld.newsapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Compose
    implementation(libs.ui)
    implementation(libs.androidx.material)
    implementation(libs.androidx.navigation.compose)

// Hilt
    implementation(libs.hilt.android)
    testImplementation(libs.androidx.runner)
    kapt(libs.hilt.android.compiler)

// Retrofit and OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

// Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

// Coil for image loading
    implementation(libs.coil.compose)
    implementation(libs.coil.compose.v222)

// Lifecycle & ViewModel
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

// Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android.v171)

// Hilt with Compose
    implementation(libs.androidx.hilt.navigation.compose)

// LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.logging.interceptor.v491)

    // MockK for mocking dependencies
    testImplementation( libs.mockk.v1135)

// Kotlin Coroutines Test for testing ViewModel coroutines
    testImplementation (libs.kotlinx.coroutines.test)

// JUnit & Assertions
    testImplementation( libs.junit)

// Hilt testing dependencies (if needed)
    androidTestImplementation( libs.hilt.android.testing)
    kaptAndroidTest (libs.hilt.android.compiler.v244)

    androidTestImplementation (libs.androidx.core)


    // MockK for mocking
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}