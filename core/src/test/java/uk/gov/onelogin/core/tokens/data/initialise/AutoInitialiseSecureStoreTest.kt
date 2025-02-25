package uk.gov.onelogin.core.tokens.data.initialise

import android.content.Context
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.securestore.AccessControlLevel
import uk.gov.android.securestore.SecureStorageConfiguration
import uk.gov.android.securestore.SecureStore
import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.tokens.domain.save.SaveTokens
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

class AutoInitialiseSecureStoreTest {
    private lateinit var useCase: AutoInitialiseSecureStore

    private val mockContext: Context = mock()
    private val mockSecureStore: SecureStore = mock()
    private val mockBioPrefHandler: BiometricPreferenceHandler = mock()
    private val mockSaveTokens: SaveTokens = mock()
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `does not initialise - when pref is null`() =
        runTest {
            whenever(mockBioPrefHandler.getBioPref()).thenReturn(null)
            useCase =
                AutoInitialiseSecureStoreImpl(
                    mockBioPrefHandler,
                    mockSaveTokens,
                    mockSecureStore,
                    mockContext,
                    dispatcher
                )

            useCase.initialise()

            verify(mockSecureStore, never()).init(any(), any())
        }

    @Test
    fun `does not initialise - when pref is NONE`() =
        runTest {
            whenever(mockBioPrefHandler.getBioPref()).thenReturn(BiometricPreference.NONE)
            useCase =
                AutoInitialiseSecureStoreImpl(
                    mockBioPrefHandler,
                    mockSaveTokens,
                    mockSecureStore,
                    mockContext,
                    dispatcher
                )

            useCase.initialise()

            verify(mockSecureStore, never()).init(any(), any())
        }

    @Test
    fun `does initialise with PASSCODE ACL - when pref is PASSCODE`() =
        runTest {
            whenever(mockBioPrefHandler.getBioPref()).thenReturn(BiometricPreference.PASSCODE)
            useCase =
                AutoInitialiseSecureStoreImpl(
                    mockBioPrefHandler,
                    mockSaveTokens,
                    mockSecureStore,
                    mockContext,
                    dispatcher
                )

            useCase.initialise()

            val expectedConfiguration =
                SecureStorageConfiguration(
                    AuthTokenStoreKeys.TOKEN_SECURE_STORE_ID,
                    AccessControlLevel.PASSCODE
                )
            verify(mockSecureStore, times(1)).init(mockContext, expectedConfiguration)
        }

    @Test
    fun `does initialise with PASSCODE_AND_CURRENT_BIOMETRICS ACL - when pref is BIOMETRICS`() =
        runTest {
            whenever(mockBioPrefHandler.getBioPref()).thenReturn(BiometricPreference.BIOMETRICS)
            useCase =
                AutoInitialiseSecureStoreImpl(
                    mockBioPrefHandler,
                    mockSaveTokens,
                    mockSecureStore,
                    mockContext,
                    dispatcher
                )

            useCase.initialise()

            val expectedConfiguration =
                SecureStorageConfiguration(
                    AuthTokenStoreKeys.TOKEN_SECURE_STORE_ID,
                    AccessControlLevel.PASSCODE_AND_CURRENT_BIOMETRICS
                )
            verify(mockSecureStore, times(1)).init(mockContext, expectedConfiguration)
        }
}
