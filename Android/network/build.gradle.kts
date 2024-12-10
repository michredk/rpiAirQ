import java.io.FileInputStream
import java.util.Properties

val propertiesFile = rootProject.file("secret.properties")
val properties = Properties()
properties.load(FileInputStream(propertiesFile))
val localPropertyValue: String = properties.getProperty("base.url")
val systemEnvValue: String = System.getenv("BASE_URL") ?: ""
rootProject.extensions.extraProperties["base_url"] = systemEnvValue

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.michredk.network"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        val baseUrl = rootProject.extra["base_url"] as String
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"${baseUrl}\"")
            buildConfigField("Boolean", "DEBUG", "false")
        }

        debug {
            buildConfigField("Boolean", "DEBUG", "true")
            buildConfigField("String", "BASE_URL", "\"${baseUrl}\"")
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(project(":model"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
    ksp (libs.androidx.hilt.compiler)

    // Retrofit : A type-safe HTTP client for Android and Java.
    implementation(libs.retrofit)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)

}