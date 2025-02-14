package uk.gov.onelogin.ui.error.generic

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
class GenericErrorScreenTest : TestCase() {
    @BindValue
    var analytics: AnalyticsLogger = mock()

    private var primaryClicked = false

    private val errorTitle = hasText(resources.getString(R.string.app_somethingWentWrongErrorTitle))
    private val errorBody = hasText(resources.getString(R.string.app_somethingWentWrongErrorBody))
    private val primaryButton = hasText(resources.getString(R.string.app_closeButton))

    @Before
    fun setUp() {
        primaryClicked = false
        composeTestRule.setContent {
            GenericErrorScreen(onClick = { primaryClicked = true })
        }
    }

    @Test
    fun genericErrorScreen() {
        composeTestRule.onNode(errorTitle).assertIsDisplayed()
        composeTestRule.onNode(errorBody).assertIsDisplayed()
        composeTestRule.onNode(primaryButton).apply {
            assertIsDisplayed()
            performClick()
        }
        assert(primaryClicked)
        verify(analytics).logEventV3Dot1(GenericErrorAnalyticsViewModel.makeScreenEvent(context))
        verify(analytics).logEventV3Dot1(GenericErrorAnalyticsViewModel.makeButtonEvent(context))
    }

    @Test
    fun onBackClicked() {
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
        verify(analytics).logEventV3Dot1(GenericErrorAnalyticsViewModel.makeBackEvent(context))
    }
}
