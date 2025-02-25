package uk.gov.onelogin.features.error.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.features.TestCase

class SignOutErrorScreenTest : TestCase() {
    private val errorTitle = hasText(resources.getString(R.string.app_signOutErrorTitle))
    private val errorBody = hasText(resources.getString(R.string.app_signOutErrorBody))
    private val primaryButton = hasText(resources.getString(R.string.app_exitButton))

    private val onExitAppClicked: () -> Unit = mock()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            SignOutErrorScreen(onExitAppClicked)
        }
    }

    @Test
    fun signOutErrorScreen() {
        composeTestRule.onNode(errorTitle).assertIsDisplayed()
        composeTestRule.onNode(errorBody).assertIsDisplayed()
        composeTestRule.onNode(primaryButton).assertIsDisplayed()
    }

    @Test
    fun clickExitButton() {
        composeTestRule.onNode(primaryButton).performClick()
        verify(onExitAppClicked).invoke()
    }
}
