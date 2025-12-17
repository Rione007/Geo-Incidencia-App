import java.util.Properties
import java.io.FileInputStream

// Lee las propiedades del archivo local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.incidenciasapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.incidenciasapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["MAPS_API_KEY"] = localProperties.getProperty("MAPS_API_KEY") ?: ""

        val properties = Properties()
        val localPropFile = project.rootProject.file("local.properties")
        if (localPropFile.exists()) {
            properties.load(localPropFile.inputStream())
        }
        val azureAccount = properties.getProperty("azure.storage.account.name") ?: ""
        val azureContainer = properties.getProperty("azure.storage.container.name") ?: ""
        val azureSas = properties.getProperty("azure.storage.sas.token") ?: ""
        buildConfigField("String", "AZURE_ACCOUNT_NAME", "\"$azureAccount\"")
        buildConfigField("String", "AZURE_CONTAINER_NAME", "\"$azureContainer\"")
        buildConfigField("String", "AZURE_SAS_TOKEN", "\"$azureSas\"")
    }

    buildFeatures {
        buildConfig = true
        compose = true
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

    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources {
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/*.kotlin_module"
            excludes += "/META-INF/io.netty.versions.properties"
            excludes += "/META-INF/DEPENDENCIES"
        }
    }
}

dependencies {
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-ktx:1.7.2")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
// OkHttp (logging)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
// Coroutines (para usar suspend)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Google Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:maps-compose:4.3.3")
    implementation("com.google.maps.android:android-maps-utils:2.3.0")
    implementation("com.google.android.material:material:1.9.0")

    //Azure
    implementation("com.azure:azure-storage-blob:12.25.1")
    implementation("com.azure:azure-core:1.45.1")

    implementation("com.google.android.material:material:1.11.0")


    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}