package uk.gov.onelogin.core.ui.pages

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import org.junit.Test
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.core.TestCase

class TitledPageTest : TestCase() {
    @Test
    fun titlePageDisplayed() {
        composeTestRule.setContent {
            TitledPage(R.string.app_homeTitle) {
                Text("test")
            }
        }

        composeTestRule.apply {
            onNodeWithText(
                resources.getString(R.string.app_homeTitle),
                useUnmergedTree = true
            ).assertIsDisplayed()

            onNodeWithText("test").assertIsDisplayed()
        }
    }
}
