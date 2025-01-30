package uk.gov.onelogin.features

import uk.gov.android.features.FeatureFlag

enum class CriCardFeatureFlag(
    override val id: String = "Cri Orchestrator Card Enabled"
) : FeatureFlag {
    ENABLED
}

enum class CriModalFeatureFlag(
    override val id: String = "Cri Orchestrator Modal Enabled"
) : FeatureFlag {
    ENABLED
}
