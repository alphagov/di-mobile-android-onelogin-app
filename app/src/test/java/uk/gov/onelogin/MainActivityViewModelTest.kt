package uk.gov.onelogin

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.authentication.login.TokenResponse
import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.data.initialise.AutoInitialiseSecureStore
import uk.gov.onelogin.extensions.CoroutinesTestExtension
import uk.gov.onelogin.extensions.InstantExecutorExtension
import uk.gov.onelogin.features.optin.data.AnalyticsOptInRepository

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class MainActivityViewModelTest {
    private val mockContext: Context = mock()
    private val analyticsOptInRepo: AnalyticsOptInRepository = mock()
    private val mockBioPrefHandler: BiometricPreferenceHandler = mock()
    private val mockTokenRepository: TokenRepository = mock()
    private val mockAutoInitialiseSecureStore: AutoInitialiseSecureStore = mock()
    private val mockLifecycleOwner: LifecycleOwner = mock()
    private val mockNavigator: Navigator = mock()

    private val testAccessToken = "testAccessToken"
    private var testIdToken: String = "testIdToken"
    private val tokenResponse = TokenResponse(
        "testType",
        testAccessToken,
        1L,
        testIdToken,
        "testRefreshToken"
    )

    private lateinit var viewModel: MainActivityViewModel

    @BeforeEach
    fun setup() {
        viewModel = MainActivityViewModel(
            analyticsOptInRepo,
            mockBioPrefHandler,
            mockTokenRepository,
            mockNavigator,
            mockAutoInitialiseSecureStore
        )
        whenever(mockContext.getString(any(), any())).thenReturn("testUrl")
        whenever(mockContext.getString(any())).thenReturn("test")
    }

    @Test
    fun `secure store auto initialised`() = runTest {
        verify(mockAutoInitialiseSecureStore).initialise()
    }

    @Test
    fun `lock screen activates when app backgrounds`() {
        // GIVEN we are logged in
        whenever(mockTokenRepository.getTokenResponse()).thenReturn(tokenResponse)
        // AND user Biometric enabled
        whenever(mockBioPrefHandler.getBioPref()).thenReturn(BiometricPreference.BIOMETRICS)

        // WHEN app goes in the background
        viewModel.onPause(owner = mockLifecycleOwner)

        // THEN token is removed from runtime memory
        verify(mockTokenRepository).clearTokenResponse()
        // AND user navigates to the lock screen (splash screen)
        verify(mockNavigator).navigate(LoginRoutes.Start)
    }

    @Test
    fun `synchronise analytics on each app start`() = runTest {
        viewModel.onStart(owner = mockLifecycleOwner)
        verify(analyticsOptInRepo).synchronise()
    }
}
