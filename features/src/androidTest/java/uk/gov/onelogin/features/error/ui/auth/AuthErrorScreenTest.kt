package uk.gov.onelogin.features.error.ui.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.features.TestCase

class AuthErrorScreenTest : TestCase() {
    private lateinit var viewModel: AuthErrorViewModel
    private val navigator: Navigator = mock()
    private val title = hasText(resources.getString(R.string.app_dataDeletedErrorTitle))
    private val intro = hasText(resources.getString(R.string.app_dataDeletedErrorBody1))
    private val content = hasText(resources.getString(R.string.app_dataDeletedErrorBody2))
    private val bulletItem1 = hasText(resources.getString(R.string.app_dataDeletedErrorBullet1))
    private val bulletItem2 = hasText(resources.getString(R.string.app_dataDeletedErrorBullet2))
    private val bulletItem3 = hasText(resources.getString(R.string.app_dataDeletedErrorBullet3))
    private val instruction = hasText(resources.getString(R.string.app_dataDeletedErrorBody3))
    private val primary = hasText(resources.getString(R.string.app_SignInWithGovUKOneLoginButton))

    @Before
    fun setup() {
        viewModel = AuthErrorViewModel(navigator)
        composeTestRule.setContent {
            AuthErrorScreen(viewModel)
        }
    }

    @Test
    fun reAuthErrorScreen() {
        composeTestRule.apply {
            onNodeWithContentDescription(
                context.getString(R.string.app_dataDeletedError_ContentDescription)
            ).assertIsDisplayed()

            onNode(title).assertIsDisplayed()
            onNode(intro).assertIsDisplayed()
            onNode(content).assertIsDisplayed()
            onNode(bulletItem1).assertIsDisplayed()
            onNode(bulletItem2).assertIsDisplayed()
            onNode(bulletItem3).assertIsDisplayed()
            onNode(instruction).assertIsDisplayed()
        }
    }

    @Test
    fun onPrimary() {
        composeTestRule.onNode(primary)
            .performClick()

        verify(navigator).navigate(LoginRoutes.Start, true)
    }
}
