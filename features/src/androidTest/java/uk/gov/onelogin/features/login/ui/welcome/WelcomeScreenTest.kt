package uk.gov.onelogin.features.login.ui.welcome

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.network.online.OnlineChecker
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.biometrics.domain.CredentialChecker
import uk.gov.onelogin.core.navigation.data.ErrorRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.data.initialise.AutoInitialiseSecureStore
import uk.gov.onelogin.core.tokens.domain.VerifyIdToken
import uk.gov.onelogin.core.tokens.domain.save.SaveTokenExpiry
import uk.gov.onelogin.core.tokens.domain.save.SaveTokens
import uk.gov.onelogin.core.ui.pages.loading.LoadingScreenAnalyticsViewModel
import uk.gov.onelogin.features.TestCase
import uk.gov.onelogin.features.login.domain.signin.loginredirect.HandleLoginRedirect
import uk.gov.onelogin.features.login.domain.signin.remotelogin.HandleRemoteLogin
import uk.gov.onelogin.features.login.ui.signin.welcome.SignInAnalyticsViewModel
import uk.gov.onelogin.features.login.ui.signin.welcome.WelcomePreview
import uk.gov.onelogin.features.login.ui.signin.welcome.WelcomeScreen
import uk.gov.onelogin.features.login.ui.signin.welcome.WelcomeScreenViewModel

class WelcomeScreenTest : TestCase() {
    private lateinit var credChecker: CredentialChecker
    private lateinit var biometricPreferenceHandler: BiometricPreferenceHandler
    private lateinit var tokenRepository: TokenRepository
    private lateinit var autoInitialiseSecureStore: AutoInitialiseSecureStore
    private lateinit var verifyIdToken: VerifyIdToken
    private lateinit var navigator: Navigator
    private lateinit var saveTokens: SaveTokens
    private lateinit var saveTokenExpiry: SaveTokenExpiry
    private lateinit var handleRemoteLogin: HandleRemoteLogin
    private lateinit var handleLoginRedirect: HandleLoginRedirect
    private lateinit var onlineChecker: OnlineChecker
    private lateinit var viewModel: WelcomeScreenViewModel
    private lateinit var analytics: AnalyticsLogger
    private lateinit var analyticsViewModel: SignInAnalyticsViewModel
    private lateinit var loadingAnalyticsVM: LoadingScreenAnalyticsViewModel

    private var shouldTryAgainCalled = false

    private val signInTitle = hasText(resources.getString(R.string.app_signInTitle))
    private val signInSubTitle = hasText(resources.getString(R.string.app_signInBody))
    private val signInButton = hasText(resources.getString(R.string.app_signInButton))
    private val signInIcon =
        hasContentDescription(resources.getString(R.string.app_signInIconDescription))

    @Before
    fun setup() {
        credChecker = mock()
        biometricPreferenceHandler = mock()
        tokenRepository = mock()
        autoInitialiseSecureStore = mock()
        verifyIdToken = mock()
        navigator = mock()
        saveTokens = mock()
        saveTokenExpiry = mock()
        handleRemoteLogin = mock()
        handleLoginRedirect = mock()
        onlineChecker = mock()
        viewModel =
            WelcomeScreenViewModel(
                context,
                credChecker,
                biometricPreferenceHandler,
                tokenRepository,
                autoInitialiseSecureStore,
                verifyIdToken,
                navigator,
                saveTokens,
                saveTokenExpiry,
                handleRemoteLogin,
                handleLoginRedirect,
                onlineChecker
            )
        analytics = mock()
        analyticsViewModel = SignInAnalyticsViewModel(context, analytics)
        loadingAnalyticsVM = LoadingScreenAnalyticsViewModel(context, analytics)
        shouldTryAgainCalled = false
    }

    @Test
    fun verifyComponents() {
        composeTestRule.setContent {
            WelcomeScreen(
                analyticsViewModel = analyticsViewModel,
                viewModel = viewModel,
                loadingAnalyticsViewModel = loadingAnalyticsVM
            )
        }

        composeTestRule.onNode(signInTitle).assertIsDisplayed()
        composeTestRule.onNode(signInSubTitle).assertIsDisplayed()
        composeTestRule.onNode(signInButton).assertIsDisplayed()
        composeTestRule.onNode(signInIcon).assertIsDisplayed()
    }

    @Test
    fun opensWebLoginViaCustomTab() =
        runBlocking {
            whenever(onlineChecker.isOnline()).thenReturn(true)
            composeTestRule.setContent {
                WelcomeScreen(
                    analyticsViewModel = analyticsViewModel,
                    viewModel = viewModel,
                    loadingAnalyticsViewModel = loadingAnalyticsVM
                )
            }

            whenWeClickSignIn()

            verify(handleRemoteLogin).login(any(), any())
        }

    @Test
    fun shouldTryAgainCalledOnPageLoad() {
        composeTestRule.setContent {
            WelcomeScreen(
                analyticsViewModel = analyticsViewModel,
                viewModel = viewModel,
                loadingAnalyticsViewModel = loadingAnalyticsVM,
                shouldTryAgain = {
                    shouldTryAgainCalled = true
                    false
                }
            )
        }
        assertTrue(shouldTryAgainCalled)
    }

    @Test
    fun loginFiresAutomaticallyIfOnlineAndShouldTryAgainIsTrue() =
        runBlocking {
            whenever(onlineChecker.isOnline()).thenReturn(true)
            composeTestRule.setContent {
                WelcomeScreen(
                    analyticsViewModel = analyticsViewModel,
                    viewModel = viewModel,
                    loadingAnalyticsViewModel = loadingAnalyticsVM,
                    shouldTryAgain = {
                        true
                    }
                )
            }

            verify(handleRemoteLogin).login(any(), any())
        }

    @Test
    fun navigateToErrorScreenIfNotOnlineAndShouldTryAgainIsTrue() =
        runBlocking {
            whenever(onlineChecker.isOnline()).thenReturn(false)
            composeTestRule.setContent {
                WelcomeScreen(
                    analyticsViewModel = analyticsViewModel,
                    viewModel = viewModel,
                    loadingAnalyticsViewModel = loadingAnalyticsVM,
                    shouldTryAgain = {
                        true
                    }
                )
            }
            verify(navigator).navigate(ErrorRoutes.Offline)
        }

    @Test
    fun opensNetworkErrorScreen() {
        givenWeAreOffline()

        whenWeClickSignIn()

        itOpensErrorScreen()
    }

    @Test
    fun screenViewAnalyticsLogOnResume() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val event = SignInAnalyticsViewModel.makeWelcomeViewEvent(context)
        composeTestRule.setContent {
            WelcomeScreen(
                analyticsViewModel = analyticsViewModel,
                viewModel = viewModel,
                loadingAnalyticsViewModel = loadingAnalyticsVM
            )
        }

        verify(analytics).logEventV3Dot1(event)
    }

    @Test
    fun signInAnalyticsLogOnSignInButton() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val event = SignInAnalyticsViewModel.makeSignInEvent(context)
        whenever(onlineChecker.isOnline()).thenReturn(true)
        composeTestRule.setContent {
            WelcomeScreen(
                analyticsViewModel = analyticsViewModel,
                viewModel = viewModel,
                loadingAnalyticsViewModel = loadingAnalyticsVM
            )
        }
        whenWeClickSignIn()
        verify(analytics).logEventV3Dot1(event)
    }

    private fun whenWeClickSignIn() {
        composeTestRule.onNode(signInButton).performClick()
    }

    private fun givenWeAreOffline() {
        whenever(onlineChecker.isOnline()).thenReturn(false)
        composeTestRule.setContent {
            WelcomeScreen(
                analyticsViewModel = analyticsViewModel,
                viewModel = viewModel,
                loadingAnalyticsViewModel = loadingAnalyticsVM
            )
        }
    }

    private fun itOpensErrorScreen() {
        verify(navigator).navigate(ErrorRoutes.Offline)
    }

    @Test
    fun previewTest() {
        // Absolute cop out
        composeTestRule.setContent {
            WelcomePreview()
        }
    }
}
