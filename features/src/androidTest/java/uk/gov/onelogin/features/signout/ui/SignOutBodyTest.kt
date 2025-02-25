package uk.gov.onelogin.features.signout.ui

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
import uk.gov.android.ui.pages.R as T
import uk.gov.onelogin.features.signout.domain.SignOutUIState

class SignOutBodyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var title: SemanticsMatcher
    private lateinit var header: SemanticsMatcher
    private lateinit var bullet1: SemanticsMatcher
    private lateinit var bullet2: SemanticsMatcher
    private lateinit var footer: SemanticsMatcher
    private lateinit var primaryButton: SemanticsMatcher
    private lateinit var closeButton: SemanticsMatcher

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        with(context) {
            title = hasText(getString(R.string.app_signOutConfirmationTitle_no_wallet))
            header =
                hasText(
                    getString(R.string.app_signOutConfirmationBody1_no_wallet),
                    substring = true
                )
            bullet1 =
                hasText(
                    getString(R.string.app_signOutConfirmationBullet1_no_wallet),
                    substring = true
                )
            bullet2 =
                hasText(
                    getString(R.string.app_signOutConfirmationBullet2_no_wallet),
                    substring = true
                )
            footer =
                hasText(
                    getString(R.string.app_signOutConfirmationBody2_no_wallet),
                    substring = true
                )
            primaryButton = hasText(getString(R.string.app_signOutButton_no_wallet))
            closeButton = hasContentDescription(getString(T.string.preview__alertPage__close))
        }
    }

    @Test
    fun verifyNoWalletUI() {
        // Given the SignOutBody Composable
        composeTestRule.setContent {
            SignOutBody(SignOutUIState.NoWallet, {}, {})
        }
        // Then the UI elements are visible
        with(composeTestRule) {
            onNode(title).assertIsDisplayed()
            onNode(header).assertIsDisplayed()
            onNode(bullet1).assertIsDisplayed()
            onNode(bullet2).assertIsDisplayed()
            onNode(footer).assertIsDisplayed()
            onNode(primaryButton).assertIsDisplayed()
            onNode(closeButton).assertIsDisplayed()
        }
    }

    @Test
    fun onClose() {
        // Given the SignOutBody Composable
        var actual = false
        composeTestRule.setContent {
            SignOutBody(
                uiState = SignOutUIState.NoWallet,
                onPrimary = {},
                onClose = { actual = true }
            )
        }
        // When clicking the `closeButton`
        composeTestRule.onNode(closeButton).performClick()
        // Then onClose() is called and the variable is true
        assertEquals(true, actual)
    }

    @Test
    fun onPrimary() {
        // Given the SignOutBody Composable
        var actual = false
        composeTestRule.setContent {
            SignOutBody(
                uiState = SignOutUIState.NoWallet,
                onPrimary = { actual = true },
                onClose = {}
            )
        }
        // When clicking the `primaryButton`
        composeTestRule.onNode(primaryButton).performClick()
        // Then onPrimary() is called and the variable is true
        assertEquals(true, actual)
    }

    @Test
    fun previewTest() {
        composeTestRule.setContent {
            SignOutPreview()
        }
    }
}
