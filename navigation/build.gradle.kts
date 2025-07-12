plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.spendscan.navigation"
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
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))

    implementation(projects.core.ui)
    implementation(projects.feature.balance)
    implementation(projects.feature.expenses)
    implementation(projects.feature.income)
    implementation(projects.feature.categories)
    implementation(projects.feature.settings)
    implementation(projects.feature.history)
    implementation(projects.feature.editBalance)
    implementation(projects.feature.manageTransaction)

    androidTestImplementation(platform(libs.androidx.compose.bom))
}
