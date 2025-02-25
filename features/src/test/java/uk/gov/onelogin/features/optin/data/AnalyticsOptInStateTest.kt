package uk.gov.onelogin.features.optin.data

import kotlin.test.Test
import kotlin.test.assertEquals

class AnalyticsOptInStateTest {
    @Test
    fun `None is unset`() {
        // Given the AnalyticsOptInState.None state
        val optInState = AnalyticsOptInState.None
        // When accessing the `boolean` property `isUnset`
        val actual = optInState.isUnset
        // Then the `boolean` value is true
        assertEquals(expected = true, actual = actual)
    }

    @Test
    fun `Yes is set (not unset)`() {
        // Given the AnalyticsOptInState.Yes state
        val optInState = AnalyticsOptInState.Yes
        // When accessing the `boolean` property `isUnset`
        val actual = optInState.isUnset
        // Then the `boolean` value is false
        assertEquals(expected = false, actual = actual)
    }

    @Test
    fun `No is set (not unset)`() {
        // Given the AnalyticsOptInState.No state
        val optInState = AnalyticsOptInState.No
        // When accessing the `boolean` property `isUnset`
        val actual = optInState.isUnset
        // Then the `boolean` value is false
        assertEquals(expected = false, actual = actual)
    }
}
