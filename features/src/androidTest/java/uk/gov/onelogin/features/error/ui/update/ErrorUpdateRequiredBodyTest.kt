package uk.gov.onelogin.features.error.ui.update

import android.content.Context
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.gov.android.onelogin.core.R

class ErrorUpdateRequiredBodyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var title: SemanticsMatcher
    private lateinit var body1: SemanticsMatcher
    private lateinit var body2: SemanticsMatcher
    private lateinit var primaryButton: SemanticsMatcher
    private lateinit var icon: SemanticsMatcher

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()

        title = hasText(context.getString(R.string.app_updateApp_Title))
        body1 = hasText(context.getString(R.string.app_updateAppBody1))
        body2 = hasText(context.getString(R.string.app_updateAppBody2))
        primaryButton = hasText(context.getString(R.string.app_updateAppButton))
        icon = hasContentDescription(context.getString(R.string.app_updateApp_ContentDescription))
    }

    @Test
    fun verifyComponents() {
        // Given
        composeTestRule.setContent {
            UpdateRequiredBody {}
        }
        // Then
        composeTestRule.onNode(title).assertIsDisplayed()
        composeTestRule.onNode(body1).assertIsDisplayed()
        composeTestRule.onNode(body2).assertIsDisplayed()
        composeTestRule.onNode(primaryButton).assertIsDisplayed()
        composeTestRule.onNode(icon).assertIsDisplayed()
    }

    @Test
    fun onUpdateApp() {
        // Given the UpdateRequiredBody Composable
        var actual = false
        composeTestRule.setContent {
            UpdateRequiredBody(
                onPrimary = { actual = true }
            )
        }
        // When clicking the `primaryButton`
        composeTestRule.onNode(primaryButton).performClick()
        // Then onUpdateApp() is called and the variable is true
        assertEquals(true, actual)
    }
}
