package uk.gov.android.featureflags

/**
 * An implementation of [FeatureFlags] that uses a [Set] as the underlying container of app
 * features.
 *
 * @param enabledFeatureFlags The private container for enabled [FeatureFlag] objects. Disabled
 * features won't be within this collection.
 */
data class InMemoryFeatureFlags(
    private var enabledFeatureFlags: MutableSet<FeatureFlag>
) : FeatureFlags, Iterable<FeatureFlag> {

    /**
     * Secondary constructor that accepts a variable length of [FeatureFlag] objects.
     *
     * Converts the parameter list into a [Set], which becomes the value of [enabledFeatureFlags].
     *
     * Example code:
     *
     * ```kotlin
     * val featureFlagContainer = InMemoryFeatureFlags(
     *     ExampleFeatures.TEST_FEATURE,
     *     ExampleFeatures.DEBUG_FEATURE
     * )
     * ```
     *
     * @param enabledFeatureFlags The parameter list of enabled features.
     */
    constructor(vararg enabledFeatureFlags: FeatureFlag) : this(
        enabledFeatureFlags.toMutableSet()
    )
    constructor(enabledFeatureFlags: Iterable<FeatureFlag>) : this(
        enabledFeatureFlags.toMutableSet()
    )

    override operator fun get(
        vararg flags: FeatureFlag
    ) = this.enabledFeatureFlags.containsAll(flags.toSet())

    override operator fun iterator() = this.enabledFeatureFlags.iterator()

    /**
     * Creates a new [FeatureFlags] object containing the existing features, as well as the provided
     * features.
     *
     * Be aware that this function doesn't internally update the existing object.
     *
     * @param flags the features to add to the existing enabled features.
     *
     * @return a new object containing the union between the existing set of enabled features and
     * the provided array of features.
     */
    operator fun plus(flags: Iterable<FeatureFlag>): InMemoryFeatureFlags {
        val enabledFeatures = mutableSetOf<FeatureFlag>().apply {
            addAll(enabledFeatureFlags)
            addAll(flags)
        }

        return InMemoryFeatureFlags(enabledFeatures)
    }

    /**
     * Creates a new [FeatureFlags] object containing the existing features, as well as the provided
     * feature.
     *
     * Be aware that this function doesn't internally update the existing object.
     *
     * @param flag the feature to add to the existing enabled features.
     *
     * @return a new object containing the union between the existing set of enabled features and
     * the provided feature.
     */
    operator fun plus(flag: FeatureFlag): InMemoryFeatureFlags = this + setOf(flag)

    /**
     * Updates the current [FeatureFlags] object by adding the elements provided by [flags].
     */
    operator fun plusAssign(flags: Iterable<FeatureFlag>) {
        enabledFeatureFlags.addAll(flags.toSet())
    }

    /**
     * Creates a new [FeatureFlags] object containing the difference between the existing feature
     * Set and the provided feature Set.
     *
     * Be aware that this function doesn't internally update the existing object.
     *
     * @param flags the features to remove from the existing object.
     *
     * @return a new object containing the difference between the existing set of enabled features
     * and the provided set of features.
     */
    operator fun minus(flags: Iterable<FeatureFlag>): InMemoryFeatureFlags {
        val enabledFeatures = mutableSetOf<FeatureFlag>().apply {
            addAll(enabledFeatureFlags)
            removeAll(flags.toSet())
        }

        return InMemoryFeatureFlags(enabledFeatures)
    }

    /**
     * Creates a new [FeatureFlags] object containing the difference between the existing feature
     * Set and the provided feature.
     *
     * Be aware that this function doesn't internally update the existing object.
     *
     * @param flag the feature to remove from the existing object.
     *
     * @return a new object containing the difference between the existing set of enabled features
     * and the provided set of features.
     */
    operator fun minus(flag: FeatureFlag): InMemoryFeatureFlags = this - setOf(flag)

    /**
     * Updates the current [FeatureFlags] object by removing the elements provided by [flags].
     */
    operator fun minusAssign(flags: Iterable<FeatureFlag>) {
        enabledFeatureFlags.removeAll(flags.toSet())
    }
}
