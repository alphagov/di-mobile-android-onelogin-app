package uk.gov.onelogin.features.optin.domain

import android.content.SharedPreferences
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
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
import org.mockito.kotlin.whenever
import uk.gov.onelogin.features.optin.data.AnalyticsOptInState
import uk.gov.onelogin.features.optin.domain.AnalyticsOptInLocalSource.Companion.DEFAULT_ORDINAL
import uk.gov.onelogin.features.optin.domain.AnalyticsOptInLocalSource.Companion.OPT_IN_KEY
import uk.gov.onelogin.features.optin.domain.source.OptInLocalSource

@ExperimentalCoroutinesApi
class AnalyticsOptInLocalSourceTest {
    private val dispatcher = StandardTestDispatcher()
    private val preferences: SharedPreferences = mock()
    private val editor: SharedPreferences.Editor = mock()
    private lateinit var source: OptInLocalSource

    @BeforeTest
    fun setUp() {
        source = AnalyticsOptInLocalSource(preferences, dispatcher)
        Dispatchers.setMain(dispatcher)
        whenever(preferences.edit()).thenReturn(editor)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Default Dispatcher`() =
        runTest {
            // Given AnalyticsOptInLocalSource with default construction
            source = AnalyticsOptInLocalSource(preferences)
            // When calling the update suspend method
            source.update(AnalyticsOptInState.None)
            // Then call the SharedPreferences.putInt with 0
            verify(preferences.edit()).putInt(OPT_IN_KEY, AnalyticsOptInState.None.ordinal)
        }

    @Test
    fun `Passed in Dispatcher is used`() =
        runTest {
            // Given AnalyticsOptInLocalSource with the test dispatcher passed in
            val passedDispatcher = spy(dispatcher)
            source = AnalyticsOptInLocalSource(preferences, passedDispatcher)
            // When calling the update suspend method
            source.update(AnalyticsOptInState.None)
            // Then the test dispatcher is used
            verify(passedDispatcher).dispatch(any(), any())
        }

    @Test
    fun `initial value is defaulted to None`() =
        runTest {
            // Given OptInLocalSource
            // When calling update with None
            whenever(preferences.getInt(OPT_IN_KEY, DEFAULT_ORDINAL))
                .thenReturn(AnalyticsOptInState.None.ordinal)
            // When calling getState()
            val actual = source.getState()
            // Then call getInt()
            verify(preferences).getInt(OPT_IN_KEY, DEFAULT_ORDINAL)
            assertEquals(expected = AnalyticsOptInState.None, actual)
        }

    @Test
    fun `update with None saves to SharedPreferences`() =
        runTest {
            // Given OptInLocalSource
            // When calling update with None
            source.update(AnalyticsOptInState.None)
            // Then call the SharedPreferences.putInt with 0
            // AND call the editor's commit
            verify(preferences.edit()).putInt(OPT_IN_KEY, AnalyticsOptInState.None.ordinal)
            verify(editor).commit()
        }

    @Test
    fun `update with No saves to SharedPreferences`() =
        runTest {
            // Given OptInLocalSource
            // When calling update with No
            source.update(AnalyticsOptInState.No)
            // Then call the SharedPreferences.putInt with 1
            // AND call the editor's commit
            verify(preferences.edit()).putInt(OPT_IN_KEY, AnalyticsOptInState.No.ordinal)
            verify(editor).commit()
        }

    @Test
    fun `update with Yes saves to SharedPreferences`() =
        runTest {
            // Given OptInLocalSource
            // When calling update with Yes
            source.update(AnalyticsOptInState.Yes)
            // Then call the SharedPreferences.putInt with 2
            // AND call the editor's commit
            verify(preferences.edit()).putInt(OPT_IN_KEY, AnalyticsOptInState.Yes.ordinal)
            verify(editor).commit()
        }
}
