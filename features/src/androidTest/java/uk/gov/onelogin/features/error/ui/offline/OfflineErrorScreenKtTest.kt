package uk.gov.onelogin.features.error.ui.offline

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.onelogin.features.TestCase

class OfflineErrorScreenKtTest : TestCase() {
    private lateinit var analytics: AnalyticsLogger
    private lateinit var viewModel: OfflineErrorAnalyticsViewModel
    private var retryClicked = false

    private val errorTitle = hasText(resources.getString(R.string.app_networkErrorTitle))
    private val errorBody = hasText(resources.getString(R.string.app_networkErrorBody))
    private val tryAgainButton = hasText(resources.getString(R.string.app_tryAgainButton))

    @Before
    fun setUp() {
        analytics = mock()
        viewModel = OfflineErrorAnalyticsViewModel(context, analytics)
        retryClicked = false
        composeTestRule.setContent {
            OfflineErrorScreen(
                analyticsViewModel = viewModel,
                onRetryClick = { retryClicked = true }
            )
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
        Espresso.pressBack()
        verify(analytics).logEventV3Dot1(OfflineErrorAnalyticsViewModel.makeBackEvent(context))
    }
}
