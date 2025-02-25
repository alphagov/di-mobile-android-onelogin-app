package uk.gov.onelogin.features.featureflags.data

import uk.gov.android.featureflags.FeatureFlag

enum class AppIntegrityFeatureFlag(
    override val id: String = "App Integrity Enabled"
) : FeatureFlag {
    ENABLED
}
