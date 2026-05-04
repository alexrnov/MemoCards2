plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "alexrnov.animememo"
    compileSdk = 36

    defaultConfig {
        applicationId = "alexrnov.animememo"
        minSdk = 24
        targetSdk = 36
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
        // включить функцию генерирации классов для доступа к виджетам в XML-разметке
        viewBinding = true
        // включить функцию Data Binding
        dataBinding = true
    }

    testOptions {
        unitTests {
            // Включение поддержки Android-ресурсов (для Robolectric)
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)

    androidTestImplementation(libs.androidx.junit.v130) // JUnit Extensions
    androidTestImplementation(libs.androidx.truth) // Truth Extensions (Truth для проверок)

    // Core библиотека
    androidTestImplementation(libs.androidx.core) // для инструментальных тестов
    testImplementation(libs.androidx.core) // для локальных

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine) // Движок для запуска тестов

    androidTestImplementation(libs.androidx.uiautomator) // Тестирование с UI Automator
    testImplementation(kotlin("test"))
    testImplementation(libs.robolectric)
}