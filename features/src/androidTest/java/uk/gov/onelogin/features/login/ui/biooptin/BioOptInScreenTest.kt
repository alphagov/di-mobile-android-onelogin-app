package uk.gov.onelogin.features.login.ui.biooptin

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.navigation.data.MainNavRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.data.initialise.AutoInitialiseSecureStore
import uk.gov.onelogin.features.TestCase
import uk.gov.onelogin.features.login.ui.signin.biooptin.BioOptInAnalyticsViewModel
import uk.gov.onelogin.features.login.ui.signin.biooptin.BioOptInViewModel
import uk.gov.onelogin.features.login.ui.signin.biooptin.BiometricsOptInScreen

class BioOptInScreenTest : TestCase() {
    private lateinit var biometricPreferenceHandler: BiometricPreferenceHandler
    private lateinit var autoInitialiseSecureStore: AutoInitialiseSecureStore
    private lateinit var navigator: Navigator
    private lateinit var viewModel: BioOptInViewModel
    private lateinit var analytics: AnalyticsLogger
    private lateinit var analyticsViewModel: BioOptInAnalyticsViewModel

    private val title = hasText(resources.getString(R.string.app_enableBiometricsTitle))
    private val content1 = hasText(resources.getString(R.string.app_enableBiometricsBody1))
    private val content2 = hasText(resources.getString(R.string.app_enableBiometricsBody2))
    private val content3 = hasText(resources.getString(R.string.app_enableBiometricsBody3))
    private val primaryButton = hasText(resources.getString(R.string.app_enableBiometricsButton))
    private val secondaryButton =
        hasText(resources.getString(R.string.app_enablePasscodeOrPatternButton))

    @Before
    fun setupNavigation() {
        biometricPreferenceHandler = mock()
        autoInitialiseSecureStore = mock()
        navigator = mock()
        viewModel =
            BioOptInViewModel(
                biometricPreferenceHandler,
                autoInitialiseSecureStore,
                navigator
            )
        analytics = mock()
        analyticsViewModel = BioOptInAnalyticsViewModel(context, analytics)
    }

    @Test
    fun verifyStrings() {
        composeTestRule.setContent {
            BiometricsOptInScreen(viewModel, analyticsViewModel)
        }
        composeTestRule.onNode(title).assertIsDisplayed()
        composeTestRule.onNode(content1).assertIsDisplayed()
        composeTestRule.onNode(content2).assertIsDisplayed()
        composeTestRule.onNode(content3).assertIsDisplayed()
        composeTestRule.onNode(primaryButton).assertIsDisplayed()
        composeTestRule.onNode(secondaryButton).assertIsDisplayed()
    }

    @Test
    fun testPrimaryButton() {
        composeTestRule.setContent {
            BiometricsOptInScreen(viewModel, analyticsViewModel)
        }
        composeTestRule.onNode(primaryButton).performClick()

        verify(navigator).navigate(MainNavRoutes.Start, true)
    }

    @Test
    fun testSecondaryButton() {
        composeTestRule.setContent {
            BiometricsOptInScreen(viewModel, analyticsViewModel)
        }
        composeTestRule.onNode(secondaryButton).performClick()

        verify(navigator).navigate(MainNavRoutes.Start, true)
    }
}
