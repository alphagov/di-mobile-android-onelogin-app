package uk.gov.onelogin.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import uk.gov.onelogin.TestCase

@HiltAndroidTest
class LoadingScreenTest : TestCase() {
    @Test
    fun verifyComponents() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        composeTestRule.onNodeWithTag(LOADING_SCREEN_BOX).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOADING_SCREEN_TEXT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOADING_SCREEN_PROGRESS_INDICATOR).assertIsDisplayed()
    }

    @Test
    fun preview() {
        composeTestRule.setContent {
            LoadingPreview()
        }
    }
}
