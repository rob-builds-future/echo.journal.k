plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // navigation
    alias(libs.plugins.jetbrains.kotlin.serialization)
    // Google für Firebase
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.rbf.echojournal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rbf.echojournal"
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "5.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = file("/Users/robinbe/Developer/Google KeyStore/Untitled")
            storePassword = "548754"
            keyAlias = "echo-release-key"
            keyPassword = "548754"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        // Core Library Desugaring aktivieren
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {


    // Desugaring-Bibliothek für Java 8-APIs
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.ads.mobile.sdk)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Firebase Auth
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    // Firebase Firestore
    implementation(libs.firebase.firestore)
    // Firebase Crashlytics
    implementation(libs.firebase.crashlytics)
    // extended icons
    implementation(libs.androidx.compose.material.icons.extended)
    // viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization)
    // api
    implementation(libs.moshi)
    implementation(libs.retrofit)
    implementation(libs.converterMoshi)
    implementation(libs.logging.interceptor)
    // async image
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // data store
    implementation(libs.androidx.datastore.preferences)
    // koin
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    // material 3
    implementation(libs.androidx.material3)

}