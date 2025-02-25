package uk.gov.android.featureflags

import androidx.annotation.VisibleForTesting

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
object FeatureFlagsTestData {
    val existingFeature: FeatureFlag = object : FeatureFlag {
        override val id: String
            get() = "Existing Test Feature"
    }

    val unitTestFeature: FeatureFlag = object : FeatureFlag {
        override val id: String
            get() = "Unit Test Feature"
    }

    val originalFeatures = InMemoryFeatureFlags(existingFeature)
    val noFeatures = InMemoryFeatureFlags()
}
