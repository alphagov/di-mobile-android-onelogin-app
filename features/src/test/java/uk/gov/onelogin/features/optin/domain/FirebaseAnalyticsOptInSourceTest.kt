package uk.gov.onelogin.features.optin.domain

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.onelogin.features.optin.data.AnalyticsOptInState
import uk.gov.onelogin.features.optin.domain.source.OptInRemoteSource

@ExperimentalCoroutinesApi
class FirebaseAnalyticsOptInSourceTest {
    private val dispatcher = StandardTestDispatcher()
    private val analytics: AnalyticsLogger = mock()
    private lateinit var source: OptInRemoteSource

    @BeforeTest
    fun setUp() {
        source = FirebaseAnalyticsOptInSource(analytics, dispatcher)
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Default Dispatcher`() =
        runTest {
            // Given FirebaseAnalyticsOptInSource with default construction
            source = FirebaseAnalyticsOptInSource(analytics)
            // When calling the update suspend method
            source.update(AnalyticsOptInState.None)
            // Then the AnalyticsLogger has analytics collection turned off
            verify(analytics).setEnabled(false)
        }

    @Test
    fun `Passed in Dispatcher is used`() =
        runTest {
            // Given FirebaseAnalyticsOptInSource with the test dispatcher passed in
            val passedDispatcher = spy(dispatcher)
            source = FirebaseAnalyticsOptInSource(analytics, passedDispatcher)
            // When calling the update suspend method
            source.update(AnalyticsOptInState.None)
            // Then the test dispatcher is used
            verify(passedDispatcher).dispatch(any(), any())
        }

    @Test
    fun `update with None turns collection off`() =
        runTest {
            // Given OptInRemoteSource
            // When calling update with None
            source.update(AnalyticsOptInState.None)
            // Then the AnalyticsLogger has analytics collection turned off
            verify(analytics).setEnabled(false)
        }

    @Test
    fun `update with No turns collection off`() =
        runTest {
            // Given OptInRemoteSource
            // When calling update with No
            source.update(AnalyticsOptInState.No)
            // Then the AnalyticsLogger has analytics collection turned off
            verify(analytics).setEnabled(false)
        }

    @Test
    fun `update with Yes turns collection on`() =
        runTest {
            // Given OptInRemoteSource
            // When calling update with Yes
            source.update(AnalyticsOptInState.Yes)
            // Then the AnalyticsLogger has analytics collection turned on
            verify(analytics).setEnabled(true)
        }
}
