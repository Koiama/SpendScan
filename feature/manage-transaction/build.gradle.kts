plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.spendscan.feature.manage_transaction"
    compileSdk = 35

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(projects.core.ui)
    implementation(projects.core.di)
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.feature.categories)

    implementation(libs.androidx.lifecycle.viewmodel.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}