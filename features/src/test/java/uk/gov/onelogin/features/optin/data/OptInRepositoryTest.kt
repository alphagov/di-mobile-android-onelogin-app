package uk.gov.onelogin.features.optin.data

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import uk.gov.onelogin.features.optin.data.AnalyticsOptInRepositoryTest.Companion.createTestAnalyticsOptInRepository

@ExperimentalCoroutinesApi
class OptInRepositoryTest {
    private lateinit var repository: OptInRepository

    @BeforeTest
    fun setUp() {
        repository = createTestAnalyticsOptInRepository()
    }

    @Test
    fun `initial isOptInPreferenceRequired true`() =
        runTest {
            // Given an unset opt in preference
            // When accessing the first value of isOptInPreferenceRequired
            val actual = repository.isOptInPreferenceRequired().first()
            // Then the expected `boolean` value is true
            assertEquals(expected = true, actual)
        }

    @Test
    fun `initial hasAnalyticsOptIn false`() =
        runTest {
            // Given an unset opt in preference
            // When accessing the first value of hasAnalyticsOptIn
            val actual = repository.hasAnalyticsOptIn().first()
            // Then the expected `boolean` value is false
            assertEquals(expected = false, actual)
        }

    @Test
    fun `calling optIn() `() =
        runTest {
            // Given an unknown opt in state
            // When calling optIn()
            repository.optIn()
            // Then hasAnalyticsOptIn is true and isOptInPreferenceRequired is false
            val hasAnalyticsOptIn = repository.hasAnalyticsOptIn().first()
            val isOptInPreferenceRequired = repository.isOptInPreferenceRequired().first()
            assertEquals(expected = true, hasAnalyticsOptIn)
            assertEquals(expected = false, isOptInPreferenceRequired)
        }

    @Test
    fun `calling optOut() `() =
        runTest {
            // Given an unknown opt in state
            // When calling optOut()
            repository.optOut()
            // Then hasAnalyticsOptIn is false and isOptInPreferenceRequired is false
            val hasAnalyticsOptIn = repository.hasAnalyticsOptIn().first()
            val isOptInPreferenceRequired = repository.isOptInPreferenceRequired().first()
            assertEquals(expected = false, hasAnalyticsOptIn)
            assertEquals(expected = false, isOptInPreferenceRequired)
        }

    @Test
    fun `calling reset() `() =
        runTest {
            // Given an unknown opt in state
            // When calling reset()
            repository.reset()
            // Then hasAnalyticsOptIn is false and isOptInPreferenceRequired is true
            val hasAnalyticsOptIn = repository.hasAnalyticsOptIn().first()
            val isOptInPreferenceRequired = repository.isOptInPreferenceRequired().first()
            assertEquals(expected = false, hasAnalyticsOptIn)
            assertEquals(expected = true, isOptInPreferenceRequired)
        }
}
