plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = AppConfig.compileSdk
    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
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
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeVersion
    }
}

dependencies {
    implementation(project(":core-common"))
    implementation(project(":core-model"))
    implementation(project(":core-database"))
    implementation(project(":core-datastore"))
    implementation(project(":core-network"))

    kapt("com.google.dagger:hilt-compiler:${Versions.hiltVersion}")
    implementation("androidx.datastore:datastore-preferences:${Versions.datastorePreferencesVersion}")
}