package uk.gov.onelogin.features.login.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.features.TestCase
import uk.gov.onelogin.features.login.ui.signin.PasscodeInfoScreen

class PasscodeInfoScreenKtTest : TestCase() {
    private var clicked: Int = 0

    @Before
    fun setupNavigation() {
        clicked = 0
        composeTestRule.setContent {
            PasscodeInfoScreen {
                clicked++
            }
        }
    }

    private val title = hasText(resources.getString(R.string.app_noPasscodePatternSetupTitle))
    private val content1 = hasText(resources.getString(R.string.app_noPasscodeSetupBody1))
    private val content2 = hasText(resources.getString(R.string.app_noPasscodeSetupBody2))
    private val button = hasText(resources.getString(R.string.app_continue))

    @Test
    fun verifyStrings() {
        composeTestRule.onNode(title).assertIsDisplayed()
        composeTestRule.onNode(content1).assertIsDisplayed()
        composeTestRule.onNode(content2).assertIsDisplayed()
        composeTestRule.onNode(button).assertIsDisplayed()
    }

    @Test
    fun testPrimaryButton() {
        composeTestRule.onNode(button).performClick()

        assertEquals(1, clicked)
    }
}
