plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

val dokkaVersion by rootProject.extra("1.9.20")
val sonarqubeVersion by rootProject.extra("4.3.0.3225")
val gradleVersion by rootProject.extra("8.4.1")
val kotlinGradleVersion by rootProject.extra("2.0.0")

dependencies {
    listOf(
        "com.android.tools.build:gradle:$gradleVersion",
        "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.1.10-1.0.30",
        "org.jetbrains.dokka:org.jetbrains.dokka.gradle.plugin:$dokkaVersion",
        "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinGradleVersion",
        "org.jetbrains.dokka:android-documentation-plugin:$dokkaVersion",
        "org.sonarqube:org.sonarqube.gradle.plugin:$sonarqubeVersion",
    ).forEach { dependency ->
        implementation(dependency)
    }
}

gradlePlugin {
    plugins {
        register("uk.gov.sonar.module-config") {
            description = """
                Configures sonar for a gradle sub-module.
                Uses 'SonarModuleConfigExtension' as a feature toggle.
            """.trimIndent()
            id = this.name
            implementationClass = "uk.gov.sonar.SonarModuleConfigPlugin"
        }
    }
}
