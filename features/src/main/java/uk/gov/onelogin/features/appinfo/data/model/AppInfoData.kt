package uk.gov.onelogin.features.appinfo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AppInfoData(
    val apps: App
) {
    @Serializable
    data class App(
        val android: AppInfo
    )

    @Serializable
    data class AppInfo(
        val minimumVersion: String,
        val releaseFlags: ReleaseFlags,
        val available: Boolean,
        val featureFlags: FeatureFlags
    )

    @Serializable
    data class ReleaseFlags(
        val walletVisibleViaDeepLink: Boolean,
        val walletVisibleIfExists: Boolean,
        val walletVisibleToAll: Boolean
    )

    @Serializable
    data class FeatureFlags(
        val appCheckEnabled: Boolean
    )
}
