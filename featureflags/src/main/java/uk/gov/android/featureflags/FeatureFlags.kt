package uk.gov.android.featureflags

/**
 * Abstraction for containers of [FeatureFlag] objects.
 */
fun interface FeatureFlags {
    /**
     * Checks whether the provided [FeatureFlag] exists within the implementation's data structure.
     *
     * Due to the use of `vararg`, it's possible to validate multiple features at once.
     *
     * Example usage:
     *
     * ```kotlin
     * val featureFlagContainer: FeatureFlags = // some implementation of the interface
     *
     * if (featureFlagContainer[ExampleFeatures.TEST_FEATURE]) {
     *     // Feature specific logic
     * }
     *
     * if (featureFlagContainer[ExampleFeatures.TEST_FEATURE, ExampleFeatures.DEBUG_FEATURE]) {
     *     // Logic specific to having both features enabled
     * }
     * ```
     *
     * @param flags The features to validate.
     *
     * @return `true` if the object contains the feature.
     */
    operator fun get(vararg flags: FeatureFlag): Boolean
}
