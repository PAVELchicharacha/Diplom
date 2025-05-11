plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    kotlin("plugin.serialization") version "2.0.20"

//    id("com.google.dagger.hilt.android")
//    id("kotlin-kapt")
//    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.diplom"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.diplom"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation(libs.firebase.auth)

    implementation(libs.material)
    implementation (libs.maps.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.database)

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2") // Для viewModelScope
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") // Для lifecycle
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Для корутин

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Основная библиотека Ktor Client
    implementation("io.ktor:ktor-client-core:2.3.5") // Основная библиотека Ktor
    implementation("io.ktor:ktor-client-cio:2.3.5")

    // Поддержка JSON (например, Gson)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.gson) // Используем Gson для сериализации
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.ktor:ktor-serialization-gson:2.3.5") // Добавьте эту строку
    implementation("io.ktor:ktor-client-content-negotiation:2.3.5")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20230300))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)


    implementation("androidx.activity:activity-compose:1.7.2") // Compose Activity
    implementation("androidx.compose.ui:ui:1.5.1") // Compose UI
    implementation("androidx.compose.material3:material3:1.1.1") // Material 3
    implementation("androidx.compose.runtime:runtime-livedata:1.5.1") // LiveData для Compose
    implementation("io.coil-kt:coil-compose:2.4.0") // Загрузка изображений

//    пока что не работает из за санкций,буду юзать яндекс
//    implementation("com.google.android.gms:play-services-maps:19.0.0")
//    implementation("com.google.maps.android:maps-compose:4.4.1")


    implementation("com.yandex.android:maps.mobile:4.1.0-full")



//    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.2"))
//    implementation("io.github.jan-tennert.supabase:postgrest-kt")
//    implementation("io.github.jan-tennert.supabase:auth-kt")
//    implementation("io.github.jan-tennert.supabase:realtime-kt")
//
//
//    implementation("io.ktor:ktor-client-android:3.0.1")
//
//
//    implementation ("com.google.dagger:hilt-android:2.54")
//    kapt ("com.google.dagger:hilt-compiler:2.54")

    //navigation
    implementation(libs.androidx.navigation.compose.v271)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}