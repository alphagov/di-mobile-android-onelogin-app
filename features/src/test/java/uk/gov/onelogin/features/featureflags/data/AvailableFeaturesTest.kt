package uk.gov.onelogin.features.featureflags.data

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfoList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.android.featureflags.FeatureFlag

class AvailableFeaturesTest {
    private val availableFeatures by lazy {
        AvailableFeatures()
    }

    @Test
    fun `All implementations of FeatureFlags are accounted for`() {
        val featureFlagList = getFeatureFlagImplementations()

        Assertions.assertEquals(TOTAL_FEATURE_FLAG_IMPLEMENTATIONS, featureFlagList.size) {
            "Found the following subclasses: ${featureFlagList.names}"
        }
    }

    @Test
    fun `All enum implementations of FeatureFlags are accounted for`() {
        val featureFlagList = getFeatureFlagImplementations()
        val foundFeatureFlags = mutableSetOf<FeatureFlag>()

        featureFlagList.forEach { classInfo ->
            if (classInfo.isEnum) {
                foundFeatureFlags.addAll(
                    classInfo.enumConstantObjects as List<out FeatureFlag>
                )
            }
        }

        val distinctFoundFeatures = foundFeatureFlags - availableFeatures
        val distinctAvailableFeatures = availableFeatures - foundFeatureFlags

        Assertions.assertTrue(distinctFoundFeatures.isEmpty()) {
            "There are found feature flags that aren't in the AvailableFlags class!: " +
                distinctFoundFeatures
        }
        Assertions.assertTrue(distinctAvailableFeatures.isEmpty()) {
            "There are available feature flags that weren't found by the classgraph library!: " +
                distinctAvailableFeatures
        }
    }

    @Test
    fun `available features contains included feature`() {
        Assertions.assertTrue(availableFeatures.contains(AppIntegrityFeatureFlag.ENABLED))
        Assertions.assertTrue(availableFeatures.contains(CriOrchestratorFeatureFlag.ENABLED))
    }

    @Test
    fun `adding feature flag`() {
        var availableFeatures = AvailableFeatures(mutableSetOf())
        Assertions.assertFalse(availableFeatures.contains(AppIntegrityFeatureFlag.ENABLED))

        availableFeatures = availableFeatures.plus(listOf(AppIntegrityFeatureFlag.ENABLED))
        Assertions.assertTrue(availableFeatures.contains(AppIntegrityFeatureFlag.ENABLED))
    }

    @Test
    fun `to String`() {
        Assertions.assertEquals(
            "AvailableFeatures[ENABLED, ENABLED, ENABLED]",
            availableFeatures.toString()
        )
    }

    @Test
    fun equals() {
        val currentAvailableFeatures = AvailableFeatures()
        val emptyAvailableFeatures = AvailableFeatures(mutableSetOf())

        Assertions.assertTrue(availableFeatures.equals(currentAvailableFeatures))
        Assertions.assertFalse(availableFeatures.equals(emptyAvailableFeatures))
        Assertions.assertFalse(availableFeatures.equals("not an AvailableFeatures object"))
    }

    private fun getFeatureFlagImplementations(): ClassInfoList {
        var result = ClassInfoList()
        val appInfo =
            ClassGraph().enableAllInfo().acceptPackages(
                "uk.gov"
            )
        try {
            val scanResult = appInfo.scan()
            result = scanResult.getClassesImplementing(FeatureFlag::class.java)
        } catch (
            @Suppress("TooGenericExceptionCaught") exception: Exception
        ) {
            Assertions.fail(exception.message)
        }

        return result
    }

    companion object {
        private const val TOTAL_FEATURE_FLAG_IMPLEMENTATIONS = 3
    }
}
