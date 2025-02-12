package uk.gov.onelogin.signOut.ui

import android.content.Context
import android.net.Uri
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import javax.inject.Inject
import javax.inject.Named
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.wheneverBlocking
import uk.gov.android.authentication.integrity.AppIntegrityManager
import uk.gov.android.authentication.integrity.appcheck.usecase.AppChecker
import uk.gov.android.authentication.integrity.appcheck.usecase.AttestationCaller
import uk.gov.android.authentication.integrity.keymanager.ECKeyManager
import uk.gov.android.authentication.integrity.keymanager.KeyStoreManager
import uk.gov.android.authentication.integrity.model.AppIntegrityConfiguration
import uk.gov.android.authentication.login.LoginSession
import uk.gov.android.authentication.login.LoginSessionConfiguration
import uk.gov.android.authentication.login.LoginSessionConfiguration.Locale
import uk.gov.android.features.FeatureFlags
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.android.network.online.OnlineChecker
import uk.gov.android.network.useragent.UserAgentGenerator
import uk.gov.android.onelogin.R
import uk.gov.android.securestore.SecureStore
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.onelogin.TestCase
import uk.gov.onelogin.appcheck.AppCheckModule
import uk.gov.onelogin.appcheck.AppIntegrity
import uk.gov.onelogin.appcheck.AttestationResult
import uk.gov.onelogin.appcheck.usecase.AppCheckUseCaseModule
import uk.gov.onelogin.core.analytics.AnalyticsModule
import uk.gov.onelogin.features.FeaturesModule
import uk.gov.onelogin.login.authentication.LoginSessionModule
import uk.gov.onelogin.navigation.Navigator
import uk.gov.onelogin.navigation.NavigatorModule
import uk.gov.onelogin.network.di.NetworkModule
import uk.gov.onelogin.signOut.SignOutModule
import uk.gov.onelogin.signOut.SignOutRoutes
import uk.gov.onelogin.signOut.domain.SignOutUseCase
import uk.gov.onelogin.tokens.Keys
import uk.gov.onelogin.ui.error.ErrorRoutes
import uk.gov.onelogin.ui.loading.LOADING_SCREEN_PROGRESS_INDICATOR

@HiltAndroidTest
@UninstallModules(
    LoginSessionModule::class,
    FeaturesModule::class,
    NetworkModule::class,
    NavigatorModule::class,
    SignOutModule::class,
    AnalyticsModule::class,
    AppCheckUseCaseModule::class,
    AppCheckModule::class
)
class SignedOutInfoScreenTest : TestCase() {
    @BindValue
    val loginSession: LoginSession = mock()

    @BindValue
    val featureFlags: FeatureFlags = mock()

    @BindValue
    val onlineChecker: OnlineChecker = mock()

    @BindValue
    val userAgentGenerator: UserAgentGenerator = mock()

    @BindValue
    val httpClient: GenericHttpClient = mock()

    @BindValue
    val mockNavigator: Navigator = mock()

    @BindValue
    val mockSignOutUseCase: SignOutUseCase = mock()

    @BindValue
    val analytics: AnalyticsLogger = mock()

    @BindValue
    val mockAppIntegrity: AppIntegrity = mock()

    @BindValue
    val mockAttestationManager: AppIntegrityManager = mock()

    @BindValue
    val mockAttestationCaller: AttestationCaller = mock()

    @BindValue
    val mockAppChecker: AppChecker = mock()

    @OptIn(ExperimentalEncodingApi::class)
    @BindValue
    val mockKeyStoreManager: KeyStoreManager = ECKeyManager()

    @BindValue
    val mockAppIntegrityConfiguration: AppIntegrityConfiguration = AppIntegrityConfiguration(
        mockAttestationCaller,
        mockAppChecker,
        mockKeyStoreManager
    )

    @Inject
    @Named("Open")
    lateinit var secureStore: SecureStore

    private var shouldTryAgainCalled = false
    private val persistentId = "id"

    private val signedOutTitle = hasText(resources.getString(R.string.app_youveBeenSignedOutTitle))
    private val signedOutBody1 = hasText(resources.getString(R.string.app_youveBeenSignedOutBody1))
    private val signedOutBody2 = hasText(resources.getString(R.string.app_youveBeenSignedOutBody2))
    private val signedOutButton =
        hasText(resources.getString(R.string.app_SignInWithGovUKOneLoginButton))

    @Before
    fun setup() = runBlocking {
        hiltRule.inject()
        shouldTryAgainCalled = false
        setPersistentId()
    }

    @Test
    fun verifyScreenDisplayed() {
        composeTestRule.setContent {
            SignedOutInfoScreen()
        }

        composeTestRule.apply {
            onNode(signedOutTitle).assertIsDisplayed()
            onNode(signedOutBody1).assertIsDisplayed()
            onNode(signedOutBody2).assertIsDisplayed()
        }
    }

    @Test
    fun opensWebLoginViaCustomTab() = runBlocking {
        whenever(onlineChecker.isOnline()).thenReturn(true)
        whenever(mockAppIntegrity.getClientAttestation())
            .thenReturn(AttestationResult.Success("Success"))

        composeTestRule.setContent {
            SignedOutInfoScreen()
        }

        whenWeClickSignIn()

        val authorizeEndpoint = Uri.parse(
            context.resources.getString(
                R.string.stsUrl,
                context.resources.getString(R.string.openIdConnectAuthorizeEndpoint)
            )
        )
        val tokenEndpoint = Uri.parse(
            context.resources.getString(
                R.string.stsUrl,
                context.resources.getString(R.string.tokenExchangeEndpoint)
            )
        )
        val redirectUri = Uri.parse(
            context.resources.getString(
                R.string.webBaseUrl,
                context.resources.getString(R.string.webRedirectEndpoint)
            )
        )
        val clientId = context.resources.getString(R.string.stsClientId)
        val loginSessionConfig = LoginSessionConfiguration(
            authorizeEndpoint = authorizeEndpoint,
            clientId = clientId,
            locale = Locale.EN,
            redirectUri = redirectUri,
            scopes = listOf(LoginSessionConfiguration.Scope.OPENID),
            tokenEndpoint = tokenEndpoint,
            persistentSessionId = persistentId
        )

        verify(loginSession).present(
            any(),
            eq(loginSessionConfig)
        )
    }

    @Test
    fun noPersistentId_OpensSignInScreen() = runBlocking {
        whenever(onlineChecker.isOnline()).thenReturn(true)
        deletePersistentId()

        composeTestRule.setContent {
            SignedOutInfoScreen()
        }

        whenWeClickSignIn()

        verify(mockSignOutUseCase).invoke(composeTestRule.activity)
        verify(mockNavigator).navigate(SignOutRoutes.ReAuthError, true)
    }

    @Test
    fun shouldTryAgainCalledOnPageLoad() {
        composeTestRule.setContent {
            SignedOutInfoScreen(
                shouldTryAgain = {
                    shouldTryAgainCalled = true
                    false
                }
            )
        }
        assertTrue(shouldTryAgainCalled)
    }

    @Test
    fun loginFiresAutomaticallyIfOnlineAndShouldTryAgainIsTrue() = runBlocking {
        whenever(onlineChecker.isOnline()).thenReturn(true)
        whenever(mockAppIntegrity.getClientAttestation())
            .thenReturn(AttestationResult.Success("Success"))

        composeTestRule.setContent {
            SignedOutInfoScreen(
                shouldTryAgain = {
                    true
                }
            )
        }

        val authorizeEndpoint = Uri.parse(
            context.resources.getString(
                R.string.stsUrl,
                context.resources.getString(R.string.openIdConnectAuthorizeEndpoint)
            )
        )
        val tokenEndpoint = Uri.parse(
            context.resources.getString(
                R.string.stsUrl,
                context.resources.getString(R.string.tokenExchangeEndpoint)
            )
        )
        val redirectUri = Uri.parse(
            context.resources.getString(
                R.string.webBaseUrl,
                context.resources.getString(R.string.webRedirectEndpoint)
            )
        )
        val clientId = context.resources.getString(R.string.stsClientId)
        val loginSessionConfig = LoginSessionConfiguration(
            authorizeEndpoint = authorizeEndpoint,
            clientId = clientId,
            locale = Locale.EN,
            redirectUri = redirectUri,
            scopes = listOf(LoginSessionConfiguration.Scope.OPENID),
            tokenEndpoint = tokenEndpoint,
            persistentSessionId = persistentId
        )

        verify(loginSession).present(
            any(),
            eq(loginSessionConfig)
        )
    }

    @Test
    fun loginFiresAutomaticallyButOffline() = runBlocking {
        whenever(onlineChecker.isOnline()).thenReturn(false)
        composeTestRule.setContent {
            SignedOutInfoScreen(
                shouldTryAgain = {
                    true
                }
            )
        }

        itOpensErrorScreen()
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
        val event = SignedOutInfoAnalyticsViewModel.makeSignedOutInfoViewEvent(context)
        composeTestRule.setContent {
            SignedOutInfoScreen()
        }

        verify(analytics).logEventV3Dot1(event)
    }

    @Test
    fun reAuthAnalyticsLogOnSignInButton() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val event = SignedOutInfoAnalyticsViewModel.makeReAuthEvent(context)
        whenever(onlineChecker.isOnline()).thenReturn(true)
        wheneverBlocking { mockAppIntegrity.getClientAttestation() }
            .thenReturn(AttestationResult.Success("Success"))
        composeTestRule.setContent {
            SignedOutInfoScreen()
        }
        whenWeClickSignIn()
        verify(analytics).logEventV3Dot1(event)
    }

    @Test
    fun loadingScreenDisplaysOnButtonClick() {
        whenever(onlineChecker.isOnline()).thenReturn(true)
        composeTestRule.setContent {
            SignedOutInfoScreen()
        }

        whenWeClickSignIn()

        composeTestRule.onNodeWithTag(LOADING_SCREEN_PROGRESS_INDICATOR).assertIsDisplayed()
    }

    private fun whenWeClickSignIn() {
        composeTestRule.onNode(signedOutButton).performClick()
    }

    private fun givenWeAreOffline() {
        whenever(onlineChecker.isOnline()).thenReturn(false)
        composeTestRule.setContent {
            SignedOutInfoScreen()
        }
    }

    private fun itOpensErrorScreen() {
        verify(mockNavigator).navigate(ErrorRoutes.Offline)
    }

    private suspend fun setPersistentId() {
        secureStore.upsert(
            key = Keys.PERSISTENT_ID_KEY,
            value = persistentId
        )
    }

    private fun deletePersistentId() {
        secureStore.delete(
            key = Keys.PERSISTENT_ID_KEY
        )
    }

    @Test
    fun previewTest() {
        composeTestRule.setContent {
            SignedOutInfoPreview()
        }
    }
}
