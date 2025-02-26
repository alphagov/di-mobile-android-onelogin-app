package uk.gov.onelogin.login.usecase

import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.wheneverBlocking
import uk.gov.android.authentication.login.TokenResponse
import uk.gov.onelogin.login.biooptin.BiometricPreference
import uk.gov.onelogin.login.biooptin.BiometricPreferenceHandler
import uk.gov.onelogin.login.state.LocalAuthStatus
import uk.gov.onelogin.repositiories.TokenRepository
import uk.gov.onelogin.tokens.Keys
import uk.gov.onelogin.tokens.usecases.GetFromTokenSecureStore
import uk.gov.onelogin.tokens.usecases.GetTokenExpiry
import uk.gov.onelogin.tokens.usecases.IsAccessTokenExpired

class HandleLocalLoginTest {
    private val mockActivity: FragmentActivity = mock()
    private val mockGetTokenExpiry: GetTokenExpiry = mock()
    private val mockTokenRepository: TokenRepository = mock()
    private val mockGetFromTokenSecureStore: GetFromTokenSecureStore = mock()
    private val mockBioPrefHandler: BiometricPreferenceHandler = mock()
    private val mockIsAccessTokenExpired: IsAccessTokenExpired = mock()

    private val useCase = HandleLocalLoginImpl(
        mockGetTokenExpiry,
        mockTokenRepository,
        mockIsAccessTokenExpired,
        mockGetFromTokenSecureStore,
        mockBioPrefHandler
    )

    @Test
    fun tokenExpiredAndNotNull_reAuthLogin() {
        whenever(mockIsAccessTokenExpired.invoke()).thenReturn(true)
        whenever(mockGetTokenExpiry.invoke()).thenReturn(1)
        whenever(mockBioPrefHandler.getBioPref()).thenReturn(BiometricPreference.PASSCODE)

        runBlocking {
            useCase(
                mockActivity
            ) {
                assertEquals(LocalAuthStatus.ReAuthSignIn, it)
            }
        }
    }

    @Test
    fun tokenExpiredAndNull_refreshLogin() {
        whenever(mockIsAccessTokenExpired.invoke()).thenReturn(true)
        whenever(mockGetTokenExpiry.invoke()).thenReturn(null)
        whenever(mockBioPrefHandler.getBioPref()).thenReturn(BiometricPreference.PASSCODE)

        runBlocking {
            useCase(
                mockActivity
            ) {
                assertEquals(LocalAuthStatus.ManualSignIn, it)
            }
        }
    }

    @Test
    fun idTokenNull_refreshLogin() {
        whenever(mockIsAccessTokenExpired.invoke()).thenReturn(false)
        whenever(mockGetTokenExpiry.invoke()).thenReturn(unexpiredTime)
        whenever(mockBioPrefHandler.getBioPref()).thenReturn(BiometricPreference.PASSCODE)
        wheneverBlocking {
            mockGetFromTokenSecureStore.invoke(
                context = any(),
                ArgumentMatchers.contains(Keys.ID_TOKEN_KEY),
                callback = any()
            )
        }.thenAnswer {
            (it.arguments[3] as (LocalAuthStatus) -> Unit).invoke(
                LocalAuthStatus.Success(
                    payload = mapOf(
                        Keys.ACCESS_TOKEN_KEY to "accessToken"
                    )
                )
            )
        }

        runBlocking {
            useCase(mockActivity) {
                assertEquals(LocalAuthStatus.ManualSignIn, it)
            }
        }
    }

    @Test
    fun bioPrefNone_refreshLogin() {
        whenever(mockIsAccessTokenExpired.invoke()).thenReturn(false)
        whenever(mockGetTokenExpiry()).thenReturn(null)
        whenever(mockBioPrefHandler.getBioPref()).thenReturn(BiometricPreference.NONE)

        runBlocking {
            useCase(mockActivity) {
                assertEquals(LocalAuthStatus.ManualSignIn, it)
            }
        }
    }

    @Test
    fun nonSuccessResponseFromGetFromSecureStore() {
        whenever(mockIsAccessTokenExpired.invoke()).thenReturn(false)
        whenever(mockBioPrefHandler.getBioPref()).thenReturn(BiometricPreference.PASSCODE)

        runBlocking {
            whenever(mockGetFromTokenSecureStore(any(), any(), callback = any())).thenAnswer {
                (it.arguments[2] as (LocalAuthStatus) -> Unit).invoke(LocalAuthStatus.ManualSignIn)
            }

            useCase(mockActivity) {
                assertEquals(LocalAuthStatus.ManualSignIn, it)
            }

            verify(mockTokenRepository, times(0)).setTokenResponse(any())
        }
    }

    @Test
    fun goodLogin() {
        val accessToken = "Token"
        val idToken = "IdToken"
        val tokenResponse = mapOf(
            Pair(Keys.ACCESS_TOKEN_KEY, accessToken),
            Pair(Keys.ID_TOKEN_KEY, idToken)
        )

        whenever(mockGetTokenExpiry()).thenReturn(unexpiredTime)
        whenever(mockIsAccessTokenExpired.invoke()).thenReturn(false)
        whenever(mockBioPrefHandler.getBioPref()).thenReturn(BiometricPreference.PASSCODE)

        runBlocking {
            whenever(
                mockGetFromTokenSecureStore(
                    context = any(),
                    ArgumentMatchers.any(),
                    callback = any()
                )
            ).thenAnswer {
                (it.arguments[2] as (LocalAuthStatus) -> Unit).invoke(
                    LocalAuthStatus.Success(tokenResponse)
                )
            }

            useCase(mockActivity) {
                assertEquals(LocalAuthStatus.Success(tokenResponse), it)
            }

            verify(mockTokenRepository).setTokenResponse(
                TokenResponse(
                    accessToken = accessToken,
                    idToken = idToken,
                    tokenType = "",
                    accessTokenExpirationTime = unexpiredTime
                )
            )
        }
    }

    companion object {
        val unexpiredTime = System.currentTimeMillis() + 100000L
    }
}
