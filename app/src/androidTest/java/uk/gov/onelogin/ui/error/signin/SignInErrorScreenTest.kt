package uk.gov.onelogin.ui.error.signin

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.R
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.onelogin.TestCase
import uk.gov.onelogin.core.analytics.AnalyticsModule

@HiltAndroidTest
@UninstallModules(AnalyticsModule::class)
class SignInErrorScreenTest : TestCase() {
    @BindValue
    var analytics: AnalyticsLogger = mock()

    private val title = hasText(resources.getString(R.string.app_signInErrorTitle))
    private val body = hasText(resources.getString(R.string.app_signInErrorBody))
    private val button = hasText(resources.getString(R.string.app_closeButton))

    private var onClick = false

    @Before
    fun setupNavigation() {
        composeTestRule.setContent {
            SignInErrorScreen { onClick = !onClick }
        }
    }

    @Test
    fun verifyComponents() {
        composeTestRule.onNode(title).assertIsDisplayed()
        composeTestRule.onNode(body).assertIsDisplayed()
        composeTestRule.onNode(button).apply {
            assertIsDisplayed()
            performClick()
        }
        assert(onClick)
        verify(analytics).logEventV3Dot1(SignInErrorAnalyticsViewModel.makeScreenEvent(context))
        verify(analytics).logEventV3Dot1(SignInErrorAnalyticsViewModel.makeButtonEvent(context))
    }

    @Test
    fun checkBackClicked() {
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
        verify(analytics).logEventV3Dot1(SignInErrorAnalyticsViewModel.makeBackEvent(context))
    }
}
