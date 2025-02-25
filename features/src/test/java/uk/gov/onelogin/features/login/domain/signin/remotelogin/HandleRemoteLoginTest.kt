package uk.gov.onelogin.features.login.domain.signin.remotelogin

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.authentication.login.LoginSession
import uk.gov.android.authentication.login.LoginSessionConfiguration
import uk.gov.onelogin.core.tokens.domain.retrieve.GetPersistentId
import uk.gov.onelogin.core.utils.LocaleUtils
import uk.gov.onelogin.core.utils.UriParser
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrity
import uk.gov.onelogin.features.login.domain.appintegrity.AttestationResult

class HandleRemoteLoginTest {
    private val mockContext: Context = mock()
    private val mockGetPersistentId: GetPersistentId = mock()
    private val mockLocaleUtils: LocaleUtils = mock()
    private val mockLoginSession: LoginSession = mock()
    private val mockAppIntegrity: AppIntegrity = mock()
    private val mockUriParser: UriParser = mock()
    private val mockLauncher: ActivityResultLauncher<Intent> = mock()
    private val mockUri: Uri = mock()
    private val testAttestation = "testAttestation"
    private val testPersistentId = "12345"

    private lateinit var handleRemoteLogin: HandleRemoteLogin

    @BeforeEach
    fun setUp() {
        handleRemoteLogin =
            HandleRemoteLoginImpl(
                mockContext,
                mockLocaleUtils,
                mockLoginSession,
                mockGetPersistentId,
                mockAppIntegrity,
                mockUriParser
            )

        whenever(mockContext.getString(any())).thenReturn("")
        whenever(mockContext.getString(any(), any<String>())).thenReturn("")
        whenever(mockLocaleUtils.getLocaleAsSessionConfig()).thenReturn(
            LoginSessionConfiguration.Locale.EN
        )
        whenever(mockUriParser.parse(any())).thenReturn(mockUri)
    }

    @Test
    fun `login() - success with persistentId`() =
        runTest {
            whenever(mockGetPersistentId.invoke()).thenReturn(testPersistentId)
            whenever(mockAppIntegrity.getClientAttestation()).thenReturn(
                AttestationResult.Success(testAttestation)
            )

            handleRemoteLogin.login(mockLauncher) {}

            argumentCaptor<LoginSessionConfiguration> {
                verify(mockLoginSession).present(eq(mockLauncher), capture())
                val capturedConfiguration = firstValue
                assertEquals(testPersistentId, capturedConfiguration.persistentSessionId)
                assertEquals(LoginSessionConfiguration.Locale.EN, capturedConfiguration.locale)
            }
        }

    @Test
    fun `login() - success without persistentId`() =
        runTest {
            whenever(mockGetPersistentId.invoke()).thenReturn(null)
            whenever(mockAppIntegrity.getClientAttestation()).thenReturn(
                AttestationResult.Success(testAttestation)
            )

            handleRemoteLogin.login(mockLauncher) {}

            argumentCaptor<LoginSessionConfiguration> {
                verify(mockLoginSession).present(eq(mockLauncher), capture())
                val capturedConfiguration = firstValue
                assertEquals(null, capturedConfiguration.persistentSessionId)
                assertEquals(LoginSessionConfiguration.Locale.EN, capturedConfiguration.locale)
            }
        }

    @Test
    fun `login() - success with empty persistentId`() =
        runTest {
            whenever(mockGetPersistentId.invoke()).thenReturn("")
            whenever(mockAppIntegrity.getClientAttestation()).thenReturn(
                AttestationResult.Success(testAttestation)
            )

            handleRemoteLogin.login(mockLauncher) {}

            argumentCaptor<LoginSessionConfiguration> {
                verify(mockLoginSession).present(eq(mockLauncher), capture())
                val capturedConfiguration = firstValue
                assertEquals(null, capturedConfiguration.persistentSessionId)
                assertEquals(LoginSessionConfiguration.Locale.EN, capturedConfiguration.locale)
            }
        }

    @Test
    fun `login() - handles AttestationResult Failure`() =
        runTest {
            whenever(mockGetPersistentId.invoke()).thenReturn(testPersistentId)
            whenever(mockAppIntegrity.getClientAttestation()).thenReturn(
                AttestationResult.Failure("error")
            )
            var checkFailed = false

            handleRemoteLogin.login(mockLauncher) {
                checkFailed = true
            }

            assert(checkFailed)
        }

    @Test
    fun `login() - handles AttestationResult NotRequired with savedAttestation`() =
        runTest {
            whenever(mockGetPersistentId.invoke()).thenReturn(testPersistentId)
            whenever(mockAppIntegrity.getClientAttestation()).thenReturn(
                AttestationResult.NotRequired(testAttestation)
            )

            handleRemoteLogin.login(mockLauncher) {}

            argumentCaptor<LoginSessionConfiguration> {
                verify(mockLoginSession).present(eq(mockLauncher), capture())
                val capturedConfiguration = firstValue
                assertEquals(testPersistentId, capturedConfiguration.persistentSessionId)
                assertEquals(LoginSessionConfiguration.Locale.EN, capturedConfiguration.locale)
            }
        }

    @Test
    fun `login() - handles AttestationResult NotRequired without savedAttestation`() =
        runTest {
            whenever(mockGetPersistentId.invoke()).thenReturn(testPersistentId)
            whenever(mockAppIntegrity.getClientAttestation()).thenReturn(
                AttestationResult.NotRequired(null)
            )

            handleRemoteLogin.login(mockLauncher) {}

            argumentCaptor<LoginSessionConfiguration> {
                verify(mockLoginSession).present(eq(mockLauncher), capture())
                val capturedConfiguration = firstValue
                assertEquals(testPersistentId, capturedConfiguration.persistentSessionId)
                assertEquals(LoginSessionConfiguration.Locale.EN, capturedConfiguration.locale)
            }
        }
}
