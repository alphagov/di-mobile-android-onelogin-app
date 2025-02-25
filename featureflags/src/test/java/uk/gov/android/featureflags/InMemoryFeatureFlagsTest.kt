package uk.gov.android.featureflags

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class InMemoryFeatureFlagsTest {

    @Test
    fun `flags may have features added in an immutable way`() {
        val updateByFlag =
            FeatureFlagsTestData.originalFeatures + FeatureFlagsTestData.unitTestFeature
        val updateBySet =
            FeatureFlagsTestData.originalFeatures + FeatureFlagsTestData.unitTestFeature

        assertAgainstOriginalFeature(updateByFlag, updateBySet)
    }

    @Test
    fun `flags may have features removed in an immutable way`() {
        val updateByFlag = FeatureFlagsTestData.originalFeatures -
            FeatureFlagsTestData.existingFeature
        val updateBySet =
            FeatureFlagsTestData.originalFeatures - FeatureFlagsTestData.existingFeature

        assertAgainstOriginalFeature(updateByFlag, updateBySet)
    }

    @Test
    fun `enabled features defined by existence within private Set`() {
        assertTrue(
            FeatureFlagsTestData.originalFeatures[FeatureFlagsTestData.existingFeature],
            "The API feature should be within the FeatureFlags object!"
        )
        assertFalse(
            FeatureFlagsTestData.originalFeatures[FeatureFlagsTestData.unitTestFeature],
            "The anonymous object should not have it's feature enabled!"
        )
        assertFalse(
            FeatureFlagsTestData.originalFeatures[
                FeatureFlagsTestData.existingFeature,
                FeatureFlagsTestData.unitTestFeature
            ],
            "The and logic should have been false due to the disabled unitTestFeature!"
        )
    }

    private fun assertAgainstOriginalFeature(
        updateByFlag: InMemoryFeatureFlags,
        updateBySet: InMemoryFeatureFlags
    ) {
        assertNotEquals(
            FeatureFlagsTestData.originalFeatures,
            updateByFlag,
            "originalFeatures should have created a different object via Flag!"
        )
        assertNotEquals(
            FeatureFlagsTestData.originalFeatures,
            updateBySet,
            "originalFeatures should have created a different object via Set!"
        )
        assertEquals(
            updateByFlag,
            updateBySet,
            "updateByFlag should value-match updateBySet!"
        )
    }
}
