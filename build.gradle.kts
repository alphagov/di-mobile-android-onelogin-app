buildscript {
    dependencies {
        listOf(
            libs.jacoco.agent,
            libs.jacoco.ant,
            libs.jacoco.core,
            libs.jacoco.report,
        ).forEach {
            classpath(it)
        }
    }

    val localProperties = java.util.Properties()
    if (rootProject.file("local.properties").exists()) {
        localProperties.load(java.io.FileInputStream(rootProject.file("local.properties")))
    }

    val getVersionCode: () -> Int = {
        val code: Int =
            if (rootProject.hasProperty("versionCode")) {
                rootProject.property("versionCode").toString().toInt()
            } else if (localProperties.getProperty("versionCode") != null) {
                localProperties.getProperty("versionCode").toString().toInt()
            } else {
                throw Error(
                    "Version code was not found as a command line parameter or a local property",
                )
            }

        println("VersionCode is set to $code")
        code
    }

    val getVersionName: () -> String = {
        val name: String =
            if (rootProject.hasProperty("versionName")) {
                rootProject.property("versionName") as String
            } else if (localProperties.getProperty("versionName") != null) {
                localProperties.getProperty("versionName") as String
            } else {
                throw Error(
                    "Version name was not found as a command line parameter or a local property",
                )
            }

        println("VersionName is set to $name")
        name
    }

    val versionCode: Int by rootProject.extra(
        getVersionCode()
    )
    val versionName: String by rootProject.extra(
        getVersionName()
    )
    val debugAppCheckToken: String by rootProject.extra(
        try {
            providers.gradleProperty("debugAppCheckToken").get()
        }  catch (e: org.gradle.api.internal.provider.MissingValueException) {
            logger.warn("firebase debug token not found in gradle properties")
            System.getenv("BUILD_DEBUG_APP_CHECK_TOKEN")
        }
    )

    // https://issuetracker.google.com/issues/380600747
    dependencies {
        classpath("org.bouncycastle:bcutil-jdk18on:1.79")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.crashlytics) apply false
    id("uk.gov.sonar.root-config")
}

setProperty("appId", "uk.gov.onelogin")
setProperty("compileSdkVersion", 35)
setProperty("configDir", "${rootProject.rootDir}/config")
setProperty("minSdkVersion", 29)
setProperty("targetSdkVersion", 35)

val jacocoVersion: String by rootProject.extra(
    libs.versions.jacoco.get(),
)

fun setProperty(
    key: String,
    value: Any,
) {
    rootProject.ext[key] = value
}

val composeVersion by project.extra("1.5.3")
val intentsVersion by project.extra("3.4.0")
val navigationVersion by project.extra("2.6.0")

apply(plugin = "lifecycle-base")
apply(plugin = "uk.gov.vale-config")
