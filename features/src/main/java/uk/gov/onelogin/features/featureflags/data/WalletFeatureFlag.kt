package uk.gov.onelogin.features.featureflags.data

import uk.gov.android.featureflags.FeatureFlag

enum class WalletFeatureFlag(override val id: String = "Wallet Enabled") : FeatureFlag {
    ENABLED
}
