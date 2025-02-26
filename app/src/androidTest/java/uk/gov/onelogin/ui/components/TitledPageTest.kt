package uk.gov.onelogin.ui.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import uk.gov.android.onelogin.R
import uk.gov.onelogin.TestCase

@HiltAndroidTest
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
