package uk.gov.onelogin.appcheck

import android.content.Context
import io.ktor.util.date.getTimeMillis
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.authentication.integrity.AppIntegrityManager
import uk.gov.android.authentication.integrity.appcheck.model.AttestationResponse
import uk.gov.android.authentication.integrity.keymanager.ECKeyManager
import uk.gov.android.authentication.integrity.pop.SignedPoP
import uk.gov.android.features.FeatureFlags
import uk.gov.android.securestore.error.SecureStorageError
import uk.gov.onelogin.appcheck.AppIntegrity.Companion.CLIENT_ATTESTATION
import uk.gov.onelogin.appcheck.AppIntegrity.Companion.CLIENT_ATTESTATION_EXPIRY
import uk.gov.onelogin.tokens.usecases.GetFromOpenSecureStore
import uk.gov.onelogin.tokens.usecases.SaveToOpenSecureStore

@OptIn(ExperimentalEncodingApi::class)
class AppIntegrityImplTest {
    private lateinit var context: Context
    private lateinit var featureFlags: FeatureFlags
    private lateinit var appCheck: AppIntegrityManager
    private lateinit var saveToOpenSecureStore: SaveToOpenSecureStore
    private lateinit var getFromOpenSecureStore: GetFromOpenSecureStore

    private lateinit var sut: AppIntegrity

    @BeforeTest
    fun setup() {
        featureFlags = mock()
        appCheck = mock()
        saveToOpenSecureStore = mock()
        getFromOpenSecureStore = mock()
        context = mock()
        sut = AppIntegrityImpl(
            context,
            featureFlags,
            appCheck,
            saveToOpenSecureStore,
            getFromOpenSecureStore
        )
    }

    @Test
    fun `get client attestation - feature flag disabled`() = runBlocking {
        whenever(featureFlags[any()]).thenReturn(false)
        val result = sut.getClientAttestation()
        assertEquals(AttestationResult.NotRequired, result)
    }

    @Test
    fun `get client attestation - attestation call successful`() = runBlocking {
        whenever(featureFlags[any()]).thenReturn(true)
        whenever(getFromOpenSecureStore.invoke(CLIENT_ATTESTATION_EXPIRY))
            .thenReturn("${getTimeMillis() + (getFiveMinInMillis())}")
        whenever(getFromOpenSecureStore.invoke(CLIENT_ATTESTATION))
            .thenReturn("testAttestation")
        whenever(appCheck.verifyAttestationJwk("testAttestation")).thenReturn(false)
        whenever(appCheck.getAttestation())
            .thenReturn(AttestationResponse.Success(SUCCESS, 0))
        whenever(appCheck.getExpiry(SUCCESS)).thenReturn(100L)
        val result = sut.getClientAttestation()

        verify(saveToOpenSecureStore).save(CLIENT_ATTESTATION, SUCCESS)
        verify(saveToOpenSecureStore).save(CLIENT_ATTESTATION_EXPIRY, "100")
        assertEquals(AttestationResult.Success, result)
    }

    @Test
    fun `get client attestation - attestation already stored in secure store`() = runBlocking {
        whenever(featureFlags[any()]).thenReturn(true)
        whenever(getFromOpenSecureStore.invoke(CLIENT_ATTESTATION_EXPIRY))
            .thenReturn("${getTimeMillis() + (getFiveMinInMillis())}")
        whenever(getFromOpenSecureStore.invoke(CLIENT_ATTESTATION))
            .thenReturn("testAttestation")
        whenever(appCheck.verifyAttestationJwk("testAttestation")).thenReturn(true)
        val result = sut.getClientAttestation()
        assertEquals(AttestationResult.NotRequired, result)
    }

    @Test
    fun `get client attestation - saved attestation does not match saved jwks`(): Unit =
        runBlocking {
            whenever(featureFlags[any()]).thenReturn(true)
            whenever(getFromOpenSecureStore.invoke(CLIENT_ATTESTATION_EXPIRY))
                .thenReturn("${getTimeMillis() + (getFiveMinInMillis())}")
            whenever(getFromOpenSecureStore.invoke(CLIENT_ATTESTATION))
                .thenReturn("testAttestation")
            whenever(appCheck.verifyAttestationJwk("testAttestation")).thenReturn(false)
            whenever(appCheck.getAttestation())
                .thenReturn(AttestationResponse.Success(SUCCESS, 0))
            sut.getClientAttestation()
            verify(appCheck).getAttestation()
        }

    @Test
    fun `get client attestation - attestation stored is expired`(): Unit = runBlocking {
        whenever(featureFlags[any()]).thenReturn(true)
        whenever(getFromOpenSecureStore.invoke(CLIENT_ATTESTATION_EXPIRY))
            .thenReturn("${(getTimeMillis() - (getFiveMinInMillis())) / 1000}")
        whenever(getFromOpenSecureStore.invoke(CLIENT_ATTESTATION))
            .thenReturn("testAttestation")
        whenever(appCheck.verifyAttestationJwk("testAttestation")).thenReturn(true)
        whenever(appCheck.getAttestation())
            .thenReturn(AttestationResponse.Success(SUCCESS, 0))
        sut.getClientAttestation()
        verify(appCheck).getAttestation()
    }

    @Test
    fun `get client attestation - attestation is not stored`(): Unit = runBlocking {
        whenever(featureFlags[any()]).thenReturn(true)
        whenever(getFromOpenSecureStore.invoke(CLIENT_ATTESTATION_EXPIRY))
            .thenReturn("${getTimeMillis() + (getFiveMinInMillis())}")
        whenever(getFromOpenSecureStore.invoke(CLIENT_ATTESTATION))
            .thenReturn(null)
        whenever(appCheck.verifyAttestationJwk("testAttestation")).thenReturn(true)
        whenever(appCheck.getAttestation())
            .thenReturn(AttestationResponse.Success(SUCCESS, 0))
        sut.getClientAttestation()
        verify(appCheck).getAttestation()
    }

    @Test
    fun `get client attestation - attestation call failure`() = runBlocking {
        whenever(featureFlags[any()]).thenReturn(true)
        whenever(appCheck.getAttestation()).thenReturn(
            AttestationResponse.Failure(reason = FAILURE, error = Exception(FAILURE))
        )
        val result = sut.getClientAttestation()
        assertEquals(AttestationResult.Failure(FAILURE), result)
    }

    @Test
    fun `get client attestation - save to secure store failure`() = runBlocking {
        val sse = SecureStorageError(Exception("Error"))
        whenever(featureFlags[any()]).thenReturn(true)
        whenever(appCheck.getAttestation())
            .thenReturn(AttestationResponse.Success(SUCCESS, 0))
        whenever(saveToOpenSecureStore.save(any(), any<String>()))
            .thenThrow(sse)
        val result = sut.getClientAttestation()

        assertEquals(AttestationResult.Failure(sse.message!!), result)
    }

    @Test
    fun `generate Proof of Possession - success`() {
        whenever(appCheck.generatePoP(any(), any()))
            .thenReturn(SignedPoP.Success(popJwt = "Success"))
        whenever(context.getString(any()))
            .thenReturn("")

        val result = sut.getProofOfPossession()
        assertEquals(SignedPoP.Success("Success"), result)
    }

    @Test
    fun `generate Proof of Possession - failure`() {
        val exp = ECKeyManager.SigningError.InvalidSignature
        whenever(appCheck.generatePoP(any(), any()))
            .thenReturn(SignedPoP.Failure(exp.message!!, exp))
        whenever(context.getString(any()))
            .thenReturn("")

        val result = sut.getProofOfPossession()
        assertEquals(SignedPoP.Failure(exp.message!!, exp), result)
    }

    companion object {
        private const val SUCCESS = "Success"
        private const val FAILURE = "Failure"

        private fun getFiveMinInMillis(): Int {
            return 5 * 60000
        }
    }
}
