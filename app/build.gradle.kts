plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.habittrackerapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.habittrackerapp"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Force dependency resolution rules to eliminate all Snyk io.netty vulnerabilities
    constraints {
        implementation("io.netty:netty-codec:4.1.133.Final") {
            because("Fixes vulnerability issues flagged in older internal build systems")
        }
        implementation("io.netty:netty-codec-http:4.1.133.Final") {
            because("Fixes vulnerability issues flagged in older internal build systems")
        }
        implementation("io.netty:netty-codec-http2:4.1.133.Final") {
            because("Fixes vulnerability issues flagged in older internal build systems")
        }
        implementation("io.netty:netty-handler:4.1.133.Final") {
            because("Fixes vulnerability issues flagged in older internal build systems")
        }
        implementation("io.netty:netty-common:4.1.133.Final") {
            because("Fixes vulnerability issues flagged in older internal build systems")
        }
    }

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
