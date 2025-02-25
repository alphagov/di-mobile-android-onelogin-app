package uk.gov.android.featureflags

/**
 * Interface for declaring a feature / sub-system within the code base.
 *
 * Enables or disables sections of the app that aren't ready for a public release, based on a
 * configuration injected by hilt.
 *
 * @property id The unique identifier for the feature.
 */
interface FeatureFlag {
    val id: String
}
