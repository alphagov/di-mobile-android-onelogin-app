package uk.gov.onelogin.features.login.ui.welcome

import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import uk.gov.android.network.online.OnlineChecker
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.biometrics.domain.CredentialChecker
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
import uk.gov.onelogin.features.login.ui.signin.welcome.WelcomeScreen
import uk.gov.onelogin.features.login.ui.signin.welcome.WelcomeScreenViewModel

class WelcomeScreenDevMenuTest : TestCase() {
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
    private lateinit var loadingAnalyticsViewModel: LoadingScreenAnalyticsViewModel

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
        loadingAnalyticsViewModel = LoadingScreenAnalyticsViewModel(context, analytics)
        composeTestRule.setContent {
            WelcomeScreen(viewModel, analyticsViewModel, loadingAnalyticsViewModel)
        }
    }

    @Test
    fun verifyDevMenuClick() {
        composeTestRule.onNode(signInIcon).performClick()

        composeTestRule.onNodeWithText("Developer Portal").assertDoesNotExist()
    }
}
