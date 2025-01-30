package uk.gov.onelogin.features

import javax.inject.Inject
import uk.gov.android.features.FeatureFlag

class AvailableFeatures(
    private val features: MutableSet<FeatureFlag>
) : Iterable<FeatureFlag> {

    @Inject
    constructor() : this(
        listOf(
            AppCheckFeatureFlag.entries.toTypedArray(),
            WalletFeatureFlag.entries.toTypedArray(),
            CriCardFeatureFlag.entries.toTypedArray(),
            CriModalFeatureFlag.entries.toTypedArray()
        ).flatMap {
            it.asIterable()
        }.toMutableSet()
    )

    operator fun contains(flag: FeatureFlag): Boolean = this.features.contains(flag)
    override operator fun iterator() = this.features.iterator()
    operator fun plus(flags: Iterable<FeatureFlag>): AvailableFeatures {
        val availableFlags = mutableSetOf<FeatureFlag>().apply {
            addAll(features)
            addAll(flags)
        }

        return AvailableFeatures(availableFlags)
    }

    override fun toString(): String = "AvailableFeatures${this.features}"
    override fun hashCode(): Int = this.features.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AvailableFeatures) return false

        if (features != other.features) return false

        return true
    }
}
