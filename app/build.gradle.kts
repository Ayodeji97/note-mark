plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.danzucker.notemark"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.danzucker.notemark"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    // Core Android libraries
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    // Splashscreen for managing app launch screens
    implementation(libs.androidx.core.splashscreen)

    // Ktor for networking
    implementation(libs.bundles.ktor)

    // Database - Room
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    // DI - Koin
    implementation(libs.bundles.koin)


    // Kotlin Serialization for navigation
    implementation(libs.kotlinx.serialization.json)

    // Allow use of java.time.Instant below API 26
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Logging
    implementation(libs.timber)

    // Test dependencies
    testImplementation(libs.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}