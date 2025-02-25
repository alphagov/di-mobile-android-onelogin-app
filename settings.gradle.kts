import org.gradle.api.internal.provider.MissingValueException

pluginManagement {
    //includeBuild("buildLogic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(
            "https://maven.pkg.github.com/govuk-one-login/mobile-android-ui",
            setupGithubCredentials()
        )
        maven(
            "https://maven.pkg.github.com/govuk-one-login/mobile-android-authentication",
            setupGithubCredentials()
        )
    }
}

fun setupGithubCredentials(): MavenArtifactRepository.() -> Unit =
    {
        val (credUser, credToken) = fetchGithubCredentials()
        credentials {
            username = credUser
            password = credToken
        }
    }

fun fetchGithubCredentials(): Pair<String, String> {
    val gprUser = providers.gradleProperty("gpr.user")
    val gprToken = providers.gradleProperty("gpr.token")

    return try {
        gprUser.get() to gprToken.get()
    } catch (exception: MissingValueException) {
        logger.warn(
            "Could not find 'Github Package Registry' properties. Refer to the proceeding " +
                "location for instructions:\n\n" +
                "${rootDir.path}/docs/developerSetup/github-authentication.md\n",
            exception
        )

        System.getenv("USERNAME") to System.getenv("TOKEN")
    }
}

// https://docs.gradle.org/8.0/userguide/kotlin_dsl.html#type-safe-accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "OneLogin-Android"
includeBuild("buildLogic")
include(":app")
include(":core")
include(":featureflags")
include(":features")

gradle.startParameter.excludedTaskNames.addAll(listOf(":buildLogic:plugins:testClasses"))
