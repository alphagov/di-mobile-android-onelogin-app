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

class SignOutBodyWalletTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var title: SemanticsMatcher
    private lateinit var header: SemanticsMatcher
    private lateinit var subtitle: SemanticsMatcher
    private lateinit var bullet1: SemanticsMatcher
    private lateinit var bullet2: SemanticsMatcher
    private lateinit var bullet3: SemanticsMatcher
    private lateinit var footer: SemanticsMatcher
    private lateinit var primaryButton: SemanticsMatcher
    private lateinit var closeButton: SemanticsMatcher

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        with(context) {
            title = hasText(getString(R.string.app_signOutConfirmationTitle))
            header = hasText(getString(R.string.app_signOutConfirmationBody1), substring = true)
            subtitle =
                hasText(
                    getString(R.string.app_signOutConfirmationSubtitle),
                    substring = true
                )
            bullet1 = hasText(getString(R.string.app_signOutConfirmationBullet1), substring = true)
            bullet2 = hasText(getString(R.string.app_signOutConfirmationBullet2), substring = true)
            bullet3 = hasText(getString(R.string.app_signOutConfirmationBullet3), substring = true)
            footer = hasText(getString(R.string.app_signOutConfirmationBody3), substring = true)
            primaryButton = hasText(getString(R.string.app_signOutAndDeleteAppDataButton))
            closeButton = hasContentDescription(getString(T.string.preview__alertPage__close))
        }
    }

    @Test
    fun verifyWalletUI() {
        // Given the SignOutBody Composable
        composeTestRule.setContent {
            SignOutBody(SignOutUIState.Wallet, {}, {})
        }
        // Then the UI elements are visible
        with(composeTestRule) {
            onNode(title).assertIsDisplayed()
            onNode(header).assertIsDisplayed()
            onNode(subtitle).assertIsDisplayed()
            onNode(bullet1).assertIsDisplayed()
            onNode(bullet2).assertIsDisplayed()
            onNode(bullet3).assertIsDisplayed()
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
                uiState = SignOutUIState.Wallet,
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
                uiState = SignOutUIState.Wallet,
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
            SignOutWalletPreview()
        }
    }
}
