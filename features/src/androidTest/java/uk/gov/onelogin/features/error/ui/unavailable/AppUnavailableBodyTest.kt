package uk.gov.onelogin.features.error.ui.unavailable

import android.content.Context
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.gov.android.onelogin.core.R

class AppUnavailableBodyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var icon: SemanticsMatcher
    private lateinit var header: SemanticsMatcher
    private lateinit var content: SemanticsMatcher

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        icon = hasTestTag(ICON_TAG)
        header = hasText(context.getString(R.string.app_appUnavailableTitle))
        content = hasText(context.getString(R.string.app_appUnavailableBody))
    }

    @Test
    fun verifyUI() {
        // Given the AppUnavailableBody Composable
        composeTestRule.setContent {
            AppUnavailableBody()
        }
        // Then the UI elements are visible
        composeTestRule.onNode(icon).assertIsDisplayed()
        composeTestRule.onNode(header).assertIsDisplayed()
        composeTestRule.onNode(content).assertIsDisplayed()
    }
}
