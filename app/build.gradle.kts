plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = rootProject.ext["appId"] as String
    compileSdk = rootProject.ext["compileSdkVersion"] as Int

    defaultConfig {
        applicationId = rootProject.ext["appId"] as String
        minSdk = rootProject.ext["minSdkVersion"] as Int
        targetSdk = rootProject.ext["targetSdkVersion"] as Int
        versionCode = rootProject.ext["versionCode"] as Int
        versionName = rootProject.ext["versionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val configDir = rootProject.extra["configDir"] as String

            storeFile = file("$configDir/keystore.jks")

            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
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
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    flavorDimensions += "env"
    productFlavors {
        listOf(
            "dev",
            "build",
            "staging",
            "integration",
            "production"
        ).forEach { environment ->
            create(environment) {
                dimension = "env"
                if (environment != "production") {
                    applicationIdSuffix = ".$environment"
                }
            }
        }
    }
}

dependencies {
    val composeVersion: String by rootProject.extra
    val intentsVersion: String by rootProject.extra
    val navigationVersion: String by rootProject.extra

    listOf(
        AndroidX.test.ext.junit,
        AndroidX.test.espresso.core,
        AndroidX.compose.ui.testJunit4,
        AndroidX.navigation.testing,
        AndroidX.test.espresso.intents
    ).forEach(::androidTestImplementation)

    listOf(
        AndroidX.compose.ui.testManifest,
        AndroidX.compose.ui.tooling
    ).forEach(::debugImplementation)

    listOf(
        AndroidX.appCompat,
        AndroidX.browser,
        AndroidX.compose.material,
        AndroidX.compose.material3,
        AndroidX.compose.ui.toolingPreview,
        AndroidX.constraintLayout,
        AndroidX.core.ktx,
        AndroidX.core.splashscreen,
        AndroidX.hilt.navigationCompose,
        Google.android.material,
        libs.components,
        libs.pages,
        libs.theme
    ).forEach(::implementation)

    listOf(
        AndroidX.navigation.fragmentKtx,
        AndroidX.navigation.uiKtx
    ).forEach(::implementation)

    testImplementation(Testing.junit4)
}

fun getVersionCode(): Int {
    val code = if (rootProject.hasProperty("versionCode")) {
        (rootProject.property("versionCode") as String).toInt()
    } else {
        1
    }
    println("VersionCode is set to $code")
    return code
}

fun getVersionName(): String {
    val name = if (rootProject.hasProperty("versionName")) {
        rootProject.property("versionName") as String
    } else {
        "1.0"
    }
    println("VersionName is set to $name")
    return name
}
