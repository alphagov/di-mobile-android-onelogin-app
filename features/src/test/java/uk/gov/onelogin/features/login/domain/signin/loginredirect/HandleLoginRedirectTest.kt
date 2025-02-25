package uk.gov.onelogin.features.login.domain.signin.loginredirect

import android.content.Intent
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import uk.gov.android.authentication.integrity.AppIntegrityParameters
import uk.gov.android.authentication.integrity.pop.SignedPoP
import uk.gov.android.authentication.login.LoginSession
import uk.gov.android.authentication.login.TokenResponse
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrity
import uk.gov.onelogin.features.login.domain.appintegrity.AttestationResult

@Suppress("MaxLineLength")
class HandleLoginRedirectTest {
    private val mockAppIntegrity: AppIntegrity = mock()
    private val mockLoginSession: LoginSession = mock()
    private val mockIntent: Intent = mock()

    private val testJwt = "testJwt"
    private val testAttestation = "testAttestation"
    private val testAccessToken = "testAccessToken"
    private var testIdToken: String = "testIdToken"
    private val tokenResponse =
        TokenResponse(
            "testType",
            testAccessToken,
            1L,
            testIdToken,
            "testRefreshToken"
        )

    private val handleLoginRedirect = HandleLoginRedirectImpl(mockAppIntegrity, mockLoginSession)

    @Test
    fun `handle() should call onSuccess with tokens when attestation and PoP are successful`() =
        runTest {
            whenever(mockAppIntegrity.retrieveSavedClientAttestation()).thenReturn(testAttestation)
            whenever(mockAppIntegrity.getProofOfPossession()).thenReturn(SignedPoP.Success(testJwt))
            whenever(mockLoginSession.finalise(any(), any(), any())).thenAnswer {
                (it.arguments[2] as (token: TokenResponse) -> Unit).invoke(tokenResponse)
            }

            // When
            handleLoginRedirect.handle(
                mockIntent,
                { },
                {
                    assertEquals(tokenResponse, it)
                }
            )
        }

    @Test
    fun `handle() should call onFailure when getProofOfPossession returns Failure`() =
        runTest {
            whenever(mockAppIntegrity.retrieveSavedClientAttestation()).thenReturn(testAttestation)
            whenever(mockAppIntegrity.getProofOfPossession()).thenReturn(
                SignedPoP.Failure(
                    "test",
                    Error("error")
                )
            )

            // When
            handleLoginRedirect.handle(
                mockIntent,
                {
                    assertEquals("error", it?.message)
                },
                { }
            )

            verifyNoInteractions(mockLoginSession)
        }

    @Test
    fun `onSuccess, savedAttestation is null and attestation and PoP are successful`() =
        runTest {
            whenever(mockAppIntegrity.retrieveSavedClientAttestation()).thenReturn(null)
            whenever(mockAppIntegrity.getClientAttestation()).thenReturn(
                AttestationResult.Success(testAttestation)
            )
            whenever(mockAppIntegrity.getProofOfPossession()).thenReturn(SignedPoP.Success(testJwt))
            whenever(mockLoginSession.finalise(any(), any(), any())).thenAnswer {
                (it.arguments[2] as (token: TokenResponse) -> Unit).invoke(tokenResponse)
                val appIntegrityParameters = it.arguments[1] as AppIntegrityParameters
                assertEquals("", appIntegrityParameters.attestation)
            }

            // When
            handleLoginRedirect.handle(
                mockIntent,
                { },
                {
                    assertEquals(tokenResponse, it)
                }
            )
        }

    @Test
    fun `onSuccess, savedAttestation is empty and attestation and PoP are successful`() =
        runTest {
            whenever(mockAppIntegrity.retrieveSavedClientAttestation()).thenReturn("")
            whenever(mockAppIntegrity.getClientAttestation()).thenReturn(
                AttestationResult.Success(testAttestation)
            )
            whenever(mockAppIntegrity.getProofOfPossession()).thenReturn(SignedPoP.Success(testJwt))
            whenever(mockLoginSession.finalise(any(), any(), any())).thenAnswer {
                (it.arguments[2] as (token: TokenResponse) -> Unit).invoke(tokenResponse)
                val appIntegrityParameters = it.arguments[1] as AppIntegrityParameters
                assertEquals("", appIntegrityParameters.attestation)
            }

            // When
            handleLoginRedirect.handle(
                mockIntent,
                { },
                {
                    assertEquals(tokenResponse, it)
                }
            )
        }

    @Test
    fun `onFailure, savedAttestation is null and getClientAttestation returns Failure`() =
        runTest {
            whenever(mockAppIntegrity.retrieveSavedClientAttestation()).thenReturn(null)
            whenever(mockAppIntegrity.getClientAttestation())
                .thenReturn(AttestationResult.Failure("error"))
            whenever(mockAppIntegrity.getProofOfPossession()).thenReturn(SignedPoP.Success(testJwt))

            // When
            handleLoginRedirect.handle(
                mockIntent,
                {
                    assertEquals("error", it?.message)
                },
                { }
            )

            verifyNoInteractions(mockLoginSession)
        }

    @Test
    fun `onSuccess, when savedAttestation is null and getClientAttestation returns NotRequired`() =
        runTest {
            whenever(mockAppIntegrity.retrieveSavedClientAttestation()).thenReturn(null)
            whenever(mockAppIntegrity.getClientAttestation())
                .thenReturn(AttestationResult.NotRequired(testAttestation))
            whenever(mockAppIntegrity.getProofOfPossession()).thenReturn(SignedPoP.Success(testJwt))
            whenever(mockLoginSession.finalise(any(), any(), any())).thenAnswer {
                (it.arguments[2] as (token: TokenResponse) -> Unit).invoke(tokenResponse)
                val appIntegrityParameters = it.arguments[1] as AppIntegrityParameters
                assertEquals(testAttestation, appIntegrityParameters.attestation)
            }

            // When
            handleLoginRedirect.handle(
                mockIntent,
                { },
                {
                    assertEquals(tokenResponse, it)
                }
            )
        }
}
