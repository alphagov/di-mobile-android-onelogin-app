package uk.gov.onelogin.core.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Test
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.core.TestCase

class EmailSectionTest : TestCase() {
    private val email = "mock@email.com"

    @Test
    fun verifyDisplay() {
        composeTestRule.setContent {
            EmailSection(email)
        }

        composeTestRule.apply {
            onNodeWithTag(DIVIDER_TEST_TAG).assertIsDisplayed()

            onNodeWithTag(IMAGE_TEST_TAG).assertIsDisplayed()

            onNodeWithText(
                context.getString(R.string.app_settingsSignInDetailsTile),
                substring = true
            ).assertIsDisplayed()

            onNodeWithText(email, substring = true).assertIsDisplayed()
        }
    }
}
