package uk.gov.onelogin.features.featureflags.data

import uk.gov.android.featureflags.FeatureFlag

enum class CriOrchestratorFeatureFlag(
    override val id: String = "Cri Orchestrator Card Enabled"
) : FeatureFlag {
    ENABLED
}
