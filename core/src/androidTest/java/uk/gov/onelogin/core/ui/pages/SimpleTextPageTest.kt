package uk.gov.onelogin.core.ui.pages

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import org.junit.Test
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.core.TestCase

class SimpleTextPageTest : TestCase() {
    @Test
    fun testSimpleTextPage() {
        val testText = "One Login"
        composeTestRule.setContent {
            SimpleTextPage(text = R.string.app_name)
        }

        composeTestRule.onNodeWithText(testText).assertIsDisplayed()
    }
}
