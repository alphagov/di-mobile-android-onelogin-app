package uk.gov.onelogin.features.optin.ui

import android.content.Context
import android.content.res.Resources
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.gov.android.onelogin.core.R

class OptInBodyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var header: SemanticsMatcher
    private lateinit var content: SemanticsMatcher
    private lateinit var privacyNotice: SemanticsMatcher
    private lateinit var primaryButton: SemanticsMatcher
    private lateinit var textButton: SemanticsMatcher

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val resources: Resources = context.resources

        header = hasText(resources.getString(R.string.app_analyticsPermissionTitle))
        content = hasText(resources.getString(R.string.app_analyticsPermissionBody))
        privacyNotice = hasTestTag(NOTICE_TAG)
        primaryButton = hasText(resources.getString(R.string.app_shareAnalyticsButton))
        textButton = hasText(resources.getString(R.string.app_doNotShareAnalytics))
    }

    @Test
    fun buttonsAreEnabledPreChoice() {
        // Given the OptInBody Composable in the OptInUIState.PreChoice state
        composeTestRule.setContent {
            OptInBody(OptInUIState.PreChoice, {}, {}, {})
        }
        // Then enable the `primaryButton` and `textButton`
        composeTestRule.onNode(primaryButton).assertIsEnabled()
        composeTestRule.onNode(textButton).assertIsEnabled()
    }

    @Test
    fun buttonsAreDisabledPostChoice() {
        // Given the OptInBody Composable in the OptInUIState.PostChoice state
        composeTestRule.setContent {
            OptInBody(OptInUIState.PostChoice, {}, {}, {})
        }
        // Then disable the `primaryButton` and `textButton`
        composeTestRule.onNode(primaryButton).assertIsNotEnabled()
        composeTestRule.onNode(textButton).assertIsNotEnabled()
    }

    @Test
    fun verifyStrings() {
        // Given the OptInBody Composable
        composeTestRule.setContent {
            OptInBody(OptInUIState.PreChoice, {}, {}, {})
        }
        // Then the UI elements are visible
        composeTestRule.onNode(header).assertIsDisplayed()
        composeTestRule.onNode(content).assertIsDisplayed()
        composeTestRule.onNode(privacyNotice).assertIsDisplayed()
        composeTestRule.onNode(primaryButton).assertIsDisplayed()
        composeTestRule.onNode(textButton).assertIsDisplayed()
    }

    @Test
    fun onShare() {
        // Given the OptInBody Composable
        var actual = false
        composeTestRule.setContent {
            OptInBody(
                uiState = OptInUIState.PreChoice,
                onPrivacyNotice = {},
                onShare = { actual = true },
                onDoNotShare = {}
            )
        }
        // When clicking the `primaryButton`
        composeTestRule.onNode(primaryButton).performClick()
        // Then onShare() is called and the variable is true
        assertEquals(true, actual)
    }

    @Test
    fun onDoNotShare() {
        // Given the OptInBody Composable
        var actual = false
        composeTestRule.setContent {
            OptInBody(
                uiState = OptInUIState.PreChoice,
                onPrivacyNotice = {},
                onShare = {},
                onDoNotShare = { actual = true }
            )
        }
        // When clicking the `textButton`
        composeTestRule.onNode(textButton).performClick()
        // Then onDoNotShare() is called and the variable is true
        assertEquals(true, actual)
    }

    @Test
    fun onPrivacyNotice() {
        // Given the OptInBody Composable
        var actual = false
        composeTestRule.setContent {
            OptInBody(
                uiState = OptInUIState.PreChoice,
                onPrivacyNotice = { actual = true },
                onShare = {},
                onDoNotShare = {}
            )
        }
        // When clicking the `privacyNotice`
        composeTestRule.onNode(privacyNotice).performClick()
        // Then onPrivacyNotice() is called and the variable is changed to true
        assertEquals(true, actual)
    }
}
