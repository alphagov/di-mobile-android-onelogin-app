package uk.gov.onelogin.features.error.ui.signin

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

class SignInErrorScreenTest : TestCase() {
    private lateinit var analytics: AnalyticsLogger
    private lateinit var viewModel: SignInErrorAnalyticsViewModel
    private var onClick = false

    private val title = hasText(resources.getString(R.string.app_signInErrorTitle))
    private val body = hasText(resources.getString(R.string.app_signInErrorBody))
    private val button = hasText(resources.getString(R.string.app_closeButton))

    @Before
    fun setupNavigation() {
        analytics = mock()
        viewModel = SignInErrorAnalyticsViewModel(context, analytics)
        composeTestRule.setContent {
            SignInErrorScreen(analyticsViewModel = viewModel) { onClick = !onClick }
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
        Espresso.pressBack()
        verify(analytics).logEventV3Dot1(SignInErrorAnalyticsViewModel.makeBackEvent(context))
    }
}
