package uk.gov.onelogin.ui.error.offline

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.R
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.onelogin.TestCase
import uk.gov.onelogin.core.analytics.AnalyticsModule

@HiltAndroidTest
@UninstallModules(AnalyticsModule::class)
class OfflineErrorScreenKtTest : TestCase() {
    @BindValue
    var analytics: AnalyticsLogger = mock()

    private var retryClicked = false

    private val errorTitle = hasText(resources.getString(R.string.app_networkErrorTitle))
    private val errorBody = hasText(resources.getString(R.string.app_networkErrorBody))
    private val tryAgainButton = hasText(resources.getString(R.string.app_tryAgainButton))

    @Before
    fun setUp() {
        retryClicked = false
        composeTestRule.setContent {
            OfflineErrorScreen(onRetryClick = { retryClicked = true })
        }
    }

    @Test
    fun offlineErrorScreen() {
        composeTestRule.onNode(errorTitle).assertIsDisplayed()
        composeTestRule.onNode(errorBody).assertIsDisplayed()
        composeTestRule.onNode(tryAgainButton).apply {
            assertIsDisplayed()
            performClick()
        }
        assert(retryClicked)
        verify(analytics).logEventV3Dot1(OfflineErrorAnalyticsViewModel.makeScreenEvent(context))
        verify(analytics).logEventV3Dot1(OfflineErrorAnalyticsViewModel.makeButtonEvent(context))
    }

    @Test
    fun onBackClicked() {
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
        verify(analytics).logEventV3Dot1(OfflineErrorAnalyticsViewModel.makeBackEvent(context))
    }
}
