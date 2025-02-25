package uk.gov.onelogin.e2e

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.compose.rememberNavController
import androidx.test.filters.FlakyTest
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import javax.inject.Inject
import javax.inject.Named
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.wheneverBlocking
import uk.gov.android.authentication.integrity.AppIntegrityManager
import uk.gov.android.authentication.integrity.appcheck.usecase.AppChecker
import uk.gov.android.authentication.integrity.appcheck.usecase.AttestationCaller
import uk.gov.android.authentication.integrity.keymanager.ECKeyManager
import uk.gov.android.authentication.integrity.keymanager.KeyStoreManager
import uk.gov.android.authentication.integrity.model.AppIntegrityConfiguration
import uk.gov.android.authentication.integrity.pop.SignedPoP
import uk.gov.android.authentication.login.LoginSession
import uk.gov.android.authentication.login.LoginSessionConfiguration
import uk.gov.android.authentication.login.TokenResponse
import uk.gov.android.onelogin.core.R
import uk.gov.android.securestore.SecureStore
import uk.gov.onelogin.HiltTestActivity
import uk.gov.onelogin.OneLoginApp
import uk.gov.onelogin.appcheck.AppCheckerModule
import uk.gov.onelogin.appinfo.AppInfoApiModule
import uk.gov.onelogin.biometrics.DeviceCredentialCheckerModule
import uk.gov.onelogin.core.biometrics.data.BiometricStatus
import uk.gov.onelogin.core.biometrics.domain.BiometricManager
import uk.gov.onelogin.core.biometrics.domain.CredentialChecker
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys
import uk.gov.onelogin.core.utils.LocaleUtils
import uk.gov.onelogin.e2e.controller.TestCase
import uk.gov.onelogin.features.appinfo.data.model.AppInfoServiceState
import uk.gov.onelogin.features.appinfo.domain.AppInfoLocalSource
import uk.gov.onelogin.features.appinfo.domain.AppInfoService
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrity
import uk.gov.onelogin.features.login.domain.appintegrity.AttestationResult
import uk.gov.onelogin.login.LoginSessionModule
import uk.gov.onelogin.login.appintegrity.AppIntegrityModule
import uk.gov.onelogin.utils.TestUtils

@HiltAndroidTest
@UninstallModules(
    LoginSessionModule::class,
    DeviceCredentialCheckerModule::class,
    AppInfoApiModule::class,
    AppCheckerModule::class,
    AppIntegrityModule::class
)
class LoginTest : TestCase() {
    @BindValue
    val mockLoginSession: LoginSession = mock()

    @BindValue
    val mockCredChecker: CredentialChecker = mock()

    @BindValue
    val mockBiometricManager: BiometricManager = mock()

    @BindValue
    val mockAppInfoService: AppInfoService = mock()

    @BindValue
    val mockAppInfoLocalSource: AppInfoLocalSource = mock()

    @Inject
    lateinit var tokenRepository: TokenRepository

    @Inject
    lateinit var localeUtils: LocaleUtils

    @Inject
    lateinit var navigator: Navigator

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

    // Remove this once Secure Store is fixed
    private val sharedPrefs = context.getSharedPreferences("SharedPrefs.key", Context.MODE_PRIVATE)

    private val data = TestUtils.appInfoData

    @get:Rule(order = 3)
    val composeRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun setup() {
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                override fun postToMainThread(runnable: Runnable) = runnable.run()

                override fun isMainThread(): Boolean = true
            })

        hiltRule.inject()
        deletePersistentId()
    }

    @After
    fun tearDown() {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @FlakyTest
    @Test
    fun selectingLoginButtonFiresAuthRequestNoPersistentId() = runTest {
        whenever(mockAppInfoService.get()).thenReturn(AppInfoServiceState.Successful(data))
        whenever(mockAppIntegrity.getClientAttestation())
            .thenReturn(AttestationResult.Success("Success"))
        whenever(mockAppIntegrity.getProofOfPossession())
            .thenReturn(SignedPoP.Success("Success"))
        tokenRepository.setTokenResponse(
            TokenResponse(
                tokenType = "type",
                accessToken = "access",
                accessTokenExpirationTime = 1L,
                idToken = ""
            )
        )
        startApp()
        clickOptOut()
        clickLogin()

        val authorizeUrl = Uri.parse(
            resources.getString(
                R.string.stsUrl,
                resources.getString(R.string.openIdConnectAuthorizeEndpoint)
            )
        )
        val redirectUrl = Uri.parse(
            resources.getString(
                R.string.webBaseUrl,
                resources.getString(R.string.webRedirectEndpoint)
            )
        )
        val tokenEndpoint = Uri.parse(
            context.resources.getString(
                R.string.stsUrl,
                context.resources.getString(
                    R.string.tokenExchangeEndpoint
                )
            )
        )
        val loginConfig = LoginSessionConfiguration(
            authorizeEndpoint = authorizeUrl,
            clientId = resources.getString(R.string.stsClientId),
            locale = localeUtils.getLocaleAsSessionConfig(),
            redirectUri = redirectUrl,
            scopes = listOf(LoginSessionConfiguration.Scope.OPENID),
            tokenEndpoint = tokenEndpoint,
            persistentSessionId = null
        )

        verify(mockLoginSession).present(any(), eq(loginConfig))
    }

    @FlakyTest
    @Test
    fun selectingLoginButtonFiresAuthRequestWithPersistentIdFromSecureStore() {
        runBlocking {
            setPersistentId()
        }
        wheneverBlocking { mockAppInfoService.get() }
            .thenReturn(AppInfoServiceState.Successful(data))
        wheneverBlocking { mockAppIntegrity.getClientAttestation() }
            .thenReturn(AttestationResult.Success("Success"))
        whenever(mockAppIntegrity.getProofOfPossession())
            .thenReturn(SignedPoP.Success("Success"))

        startApp()
        clickOptOut()
        clickLogin()

        val authorizeUrl = Uri.parse(
            resources.getString(
                R.string.stsUrl,
                resources.getString(R.string.openIdConnectAuthorizeEndpoint)
            )
        )
        val redirectUrl = Uri.parse(
            resources.getString(
                R.string.webBaseUrl,
                resources.getString(R.string.webRedirectEndpoint)
            )
        )
        val tokenEndpoint = Uri.parse(
            context.resources.getString(
                R.string.stsUrl,
                context.resources.getString(
                    R.string.tokenExchangeEndpoint
                )
            )
        )
        val loginConfig = LoginSessionConfiguration(
            authorizeEndpoint = authorizeUrl,
            clientId = resources.getString(R.string.stsClientId),
            locale = localeUtils.getLocaleAsSessionConfig(),
            redirectUri = redirectUrl,
            scopes = listOf(LoginSessionConfiguration.Scope.OPENID),
            tokenEndpoint = tokenEndpoint,
            persistentSessionId = PERSISTENT_ID
        )

        verify(mockLoginSession).present(any(), eq(loginConfig))
    }

    // App remains on sign in page when not data is returned in intent from login
    @FlakyTest
    @Test
    fun handleActivityResultNullData() {
        wheneverBlocking { mockAppInfoService.get() }
            .thenReturn(AppInfoServiceState.Successful(data))
        wheneverBlocking { mockAppIntegrity.getClientAttestation() }
            .thenReturn(AttestationResult.Success("Success"))
        whenever(mockAppIntegrity.getProofOfPossession())
            .thenReturn(SignedPoP.Success("Success"))
        setupActivityForResult(
            Intent()
        )

        clickOptOut()
        clickLogin()

        nodeWithTextExists(resources.getString(R.string.app_signInTitle))
        verify(mockLoginSession, times(0)).finalise(any(), any(), any())
    }

    @FlakyTest
    @Test
    fun handleActivityResultWithDataButLoginThrows() {
        wheneverBlocking { mockAppInfoService.get() }
            .thenReturn(AppInfoServiceState.Successful(data))
        wheneverBlocking { mockAppIntegrity.getClientAttestation() }
            .thenReturn(AttestationResult.Success("Success"))
        whenever(mockAppIntegrity.getProofOfPossession())
            .thenReturn(SignedPoP.Success("Success"))
        whenever(mockLoginSession.finalise(any(), any(), any())).thenThrow(Error())
        setupActivityForResult(
            Intent(
                Intent.ACTION_VIEW,
                Uri.EMPTY
            )
        )
        clickOptOut()
        composeRule.onNodeWithText("Sign in").performClick()
        nodeWithTextExists("There was a problem signing you in")
    }

    @FlakyTest
    @Test
    fun handleActivityResultWithDataUnsecured() {
        wheneverBlocking { mockAppInfoService.get() }
            .thenReturn(AppInfoServiceState.Successful(data))
        wheneverBlocking { mockAppIntegrity.getClientAttestation() }
            .thenReturn(AttestationResult.Success("Success"))
        whenever(mockAppIntegrity.getProofOfPossession())
            .thenReturn(SignedPoP.Success("Success"))
        mockGoodLogin()
        whenever(mockCredChecker.isDeviceSecure()).thenReturn(false)
        setupActivityForResult(
            Intent(
                Intent.ACTION_VIEW,
                Uri.EMPTY
            )
        )

        clickOptOut()
        clickLogin()
        nodeWithTextExists(resources.getString(R.string.app_noPasscodePatternSetupTitle))
        composeRule.onNodeWithText(resources.getString(R.string.app_continue))
        nodeWithTextExists(resources.getString(R.string.app_homeTitle))
    }

    @FlakyTest
    @Test
    fun handleActivityResultWithDataBioOptIn() {
        wheneverBlocking { mockAppInfoService.get() }
            .thenReturn(AppInfoServiceState.Successful(data))
        wheneverBlocking { mockAppIntegrity.getClientAttestation() }
            .thenReturn(AttestationResult.Success("Success"))
        whenever(mockAppIntegrity.getProofOfPossession())
            .thenReturn(SignedPoP.Success("Success"))
        mockGoodLogin()
        whenever(mockCredChecker.isDeviceSecure()).thenReturn(true)
        whenever(mockCredChecker.biometricStatus()).thenReturn(BiometricStatus.SUCCESS)
        setupActivityForResult(
            Intent(Intent.ACTION_VIEW, Uri.EMPTY)
        )
        clickOptOut()
        clickLogin()

        nodeWithTextExists(resources.getString(R.string.app_enableBiometricsTitle))
        composeRule.onNodeWithText(resources.getString(R.string.app_enableBiometricsButton))
        nodeWithTextExists(resources.getString(R.string.app_homeTitle))
    }

    @FlakyTest
    @Test
    fun handleActivityResultWithDataPasscode() {
        wheneverBlocking { mockAppInfoService.get() }
            .thenReturn(AppInfoServiceState.Successful(data))
        wheneverBlocking { mockAppIntegrity.getClientAttestation() }
            .thenReturn(AttestationResult.Success("Success"))
        whenever(mockAppIntegrity.getProofOfPossession())
            .thenReturn(SignedPoP.Success("Success"))
        mockGoodLogin()
        whenever(mockCredChecker.isDeviceSecure()).thenReturn(true)
        whenever(mockCredChecker.biometricStatus()).thenReturn(BiometricStatus.UNKNOWN)
        setupActivityForResult(
            Intent(Intent.ACTION_VIEW, Uri.EMPTY)
        )

        clickOptOut()
        clickLogin()

        nodeWithTextExists(resources.getString(R.string.app_homeTitle))
    }

    private fun setupActivityForResult(returnedIntent: Intent) {
        whenever(mockLoginSession.present(any(), any())).thenAnswer {
            @Suppress("unchecked_cast")
            (it.arguments[0] as ActivityResultLauncher<Intent>).launch(Intent())
        }

        composeRule.setContent {
            val registryOwner = object : ActivityResultRegistryOwner {
                override val activityResultRegistry: ActivityResultRegistry
                    get() = object : ActivityResultRegistry() {
                        override fun <I : Any?, O : Any?> onLaunch(
                            requestCode: Int,
                            contract: ActivityResultContract<I, O>,
                            input: I,
                            options: ActivityOptionsCompat?
                        ) {
                            this.dispatchResult(requestCode, Activity.RESULT_OK, returnedIntent)
                        }
                    }
            }
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                val navController = rememberNavController()

                DisposableEffect(key1 = navController) {
                    navigator.setController(navController)

                    onDispose {
                        navigator.reset()
                    }
                }

                OneLoginApp(navController = navController)
            }
        }
    }

    private fun startApp() {
        composeRule.setContent {
            val navController = rememberNavController()

            DisposableEffect(key1 = navController) {
                navigator.setController(navController)

                onDispose {
                    navigator.reset()
                }
            }

            OneLoginApp(navController = navController)
        }
    }

    private fun clickOptOut() {
        composeRule.waitForIdle()
        val doNotShare = composeRule.onNodeWithText(
            resources.getString(R.string.app_doNotShareAnalytics)
        )
        val isOnOptInScreen = doNotShare.isDisplayed()
        if (isOnOptInScreen) {
            doNotShare.performClick()
        }
    }

    private fun clickLogin() {
        composeRule.waitUntil(TIMEOUT) {
            composeRule.onNodeWithText(resources.getString(R.string.app_signInButton))
                .isDisplayed()
        }
        composeRule.onNodeWithText(resources.getString(R.string.app_signInButton)).performClick()
    }

    private fun nodeWithTextExists(text: String) {
        composeRule.waitForIdle()
        composeRule.onNodeWithText(text).isDisplayed()
    }

    private fun mockGoodLogin() {
        whenever(mockLoginSession.finalise(any(), any(), any())).thenAnswer {
            @Suppress("unchecked_cast")
            (it.arguments[2] as (TokenResponse) -> Unit).invoke(tokenResponse)
        }
    }

    private suspend fun setPersistentId() {
        secureStore.upsert(
            key = AuthTokenStoreKeys.PERSISTENT_ID_KEY,
            value = PERSISTENT_ID
        )
        sharedPrefs.edit().putString(AuthTokenStoreKeys.PERSISTENT_ID_KEY, PERSISTENT_ID).apply()
    }

    private fun deletePersistentId() {
        secureStore.delete(
            key = AuthTokenStoreKeys.PERSISTENT_ID_KEY
        )
    }

    companion object {
        private const val TIMEOUT = 10000L
        private const val PERSISTENT_ID = "cc893ece-b6bd-444d-9bb4-dec6f5778e50"
        private val tokenResponse = TokenResponse(
            tokenType = "test",
            accessToken = "test",
            accessTokenExpirationTime = 1L,
            idToken = "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjE2ZGI2NTg3LTU0NDUtNDVkNi1hN" +
                "2Q5LTk4NzgxZWJkZjkzZCJ9.eyJhdWQiOiJHRVV6a0V6SVFVOXJmYmdBWmJzal9fMUVOUU0iLCJ" +
                "pc3MiOiJodHRwczovL3Rva2VuLmJ1aWxkLmFjY291bnQuZ292LnVrIiwic3ViIjoiOWQwZjIxZG" +
                "UtMmZkNy00MjdiLWE2ZGYtMDdjZDBkOTVlM2I2IiwicGVyc2lzdGVudF9pZCI6ImNjODkzZWNlL" +
                "WI2YmQtNDQ0ZC05YmI0LWRlYzZmNTc3OGU1MCIsImlhdCI6MTcyMTk5ODE3OCwiZXhwIjoxNzIx" +
                "OTk4MzU4LCJub25jZSI6InRlc3Rfbm9uY2UiLCJlbWFpbCI6Im1vY2tAZW1haWwuY29tIiwiZW1" +
                "haWxfdmVyaWZpZWQiOnRydWV9.G1uQ9z2i-214kEmmtK7hEHRsgqJdk7AXjz_CaJDiuuqSyHZ4W" +
                "48oE1karDBA-pKWpADdBpHeUC-eCjjfBObjOg"
        )
    }
}
