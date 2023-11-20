import com.android.build.gradle.internal.tasks.MergeJavaResWorkAction

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("org.jlleitschuh.gradle.ktlint")
    id("uk.gov.onelogin.jvm-toolchains")
    id("com.google.dagger.hilt.android")

    kotlin("kapt")
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

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        testInstrumentationRunner = "uk.gov.onelogin.InstrumentationTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
                var suffix = ""

                dimension = "env"

                if (environment != "production") {
                    suffix = ".$environment"
                    applicationIdSuffix = ".$environment"
                }

                manifestPlaceholders["flavorSuffix"] = suffix
            }
        }
    }

    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/INDEX.LIST")
        resources.excludes.add("META-INF/gradle/incremental.annotation.processors")
        resources.excludes.add("META-INF/io.netty.versions.properties")
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        animationsDisabled = true
        unitTests.all {
            it.useJUnitPlatform()
            it.testLogging {
                events = setOf(
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
                )
            }
        }
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    listOf(
        AndroidX.test.ext.junit,
        AndroidX.test.espresso.core,
        AndroidX.compose.ui.testJunit4,
        AndroidX.navigation.testing,
        AndroidX.test.espresso.intents,
        libs.allure.kotlin.android,
        libs.allure.kotlin.commons,
        libs.allure.kotlin.junit4,
        libs.allure.kotlin.model,
//        libs.espresso.accessibility,
        libs.hamcrest,
        libs.hilt.android.compiler,
        libs.hilt.android.testing,
        libs.junit.foundation
    ).forEach(::androidTestImplementation)

    /**
     * Workaround for `Duplicate class org.checkerframework.checker` errors
     * @see https://github.com/android/android-test/issues/861
     */
    androidTestImplementation(libs.espresso.accessibility) {
        exclude(group = "org.checkerframework", module = "checker")
    }

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
        AndroidX.navigation.fragmentKtx,
        AndroidX.navigation.uiKtx,
        Google.android.material,
        Google.dagger.hilt.android,
        libs.components,
        libs.grpc.all,
        libs.gson,
        libs.kotlinx.serialization.json,
        libs.ktor.client.android,
        libs.netty5.all,
        libs.pages,
        libs.perfmark.api,
        libs.perfmark.traceviewer,
        libs.theme,
        libs.tracing
    ).forEach(::implementation)

    listOf(
        Google.dagger.hilt.compiler
    ).forEach(::kapt)

    listOf(
        Testing.junit.jupiter,
        Testing.junit4,
        libs.allure.kotlin.commons,
        libs.allure.kotlin.junit4,
        libs.allure.kotlin.model,
        libs.hilt.android.testing,
        libs.ktor.client.mock,
        libs.mockito.kotlin
    ).forEach(::testImplementation)
}

hilt {
    enableAggregatingTask = true
}

kapt {
    correctErrorTypes = true
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
