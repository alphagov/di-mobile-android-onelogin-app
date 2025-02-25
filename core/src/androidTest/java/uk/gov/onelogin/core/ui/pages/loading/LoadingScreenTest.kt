package uk.gov.onelogin.core.ui.pages.loading

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.Espresso
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.onelogin.core.TestCase

class LoadingScreenTest : TestCase() {
    private lateinit var analytics: AnalyticsLogger
    private lateinit var viewModel: LoadingScreenAnalyticsViewModel
    private var onBackPress = 0

    @Before
    fun setup() {
        analytics = mock()
        viewModel = LoadingScreenAnalyticsViewModel(context, analytics)
    }

    @Test
    fun verifyComponents() {
        composeTestRule.setContent {
            LoadingScreen(viewModel) { onBackPress++ }
        }

        composeTestRule.onNodeWithTag(LOADING_SCREEN_BOX).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOADING_SCREEN_TEXT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOADING_SCREEN_PROGRESS_INDICATOR).assertIsDisplayed()

        Espresso.pressBack()

        assertEquals(1, onBackPress)
    }

    @Test
    fun preview() {
        composeTestRule.setContent {
            LoadingPreview()
        }

        composeTestRule.onNodeWithTag(LOADING_SCREEN_BOX).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOADING_SCREEN_TEXT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOADING_SCREEN_PROGRESS_INDICATOR).assertIsDisplayed()
    }
}
