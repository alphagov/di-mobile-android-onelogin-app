package uk.gov.onelogin.features.error.ui.unavailable

import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.onelogin.features.TestCase

class AppUnavailableScreenTest : TestCase() {
    private lateinit var analytics: AnalyticsLogger
    private lateinit var viewModel: AppUnavailableAnalyticsViewModel

    @Before
    fun setUp() {
        analytics = mock()
        viewModel = AppUnavailableAnalyticsViewModel(context, analytics)
    }

    @Test
    fun screenViewAnalyticsLogOnResume() {
        composeTestRule.setContent {
            AppUnavailableScreen(analyticsViewModel = viewModel)
        }
        val event = AppUnavailableAnalyticsViewModel.makeUnavailableViewEvent(context)

        verify(analytics).logEventV3Dot1(event)
    }

    @Test
    fun onHardwareBackButton() {
        composeTestRule.setContent {
            AppUnavailableScreen(analyticsViewModel = viewModel)
        }
        val event = AppUnavailableAnalyticsViewModel.makeBackEvent(context)
        composeTestRule.waitForIdle()
        try {
            Espresso.pressBack()
        } catch (_: NoActivityResumedException) {
        }

        verify(analytics).logEventV3Dot1(event)
    }
}
