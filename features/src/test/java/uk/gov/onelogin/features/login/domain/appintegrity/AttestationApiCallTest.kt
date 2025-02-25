package uk.gov.onelogin.features.login.domain.appintegrity

import android.content.Context
import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.gov.android.authentication.integrity.appcheck.model.AttestationResponse
import uk.gov.android.authentication.integrity.appcheck.usecase.AttestationCaller
import uk.gov.android.authentication.json.jwk.JWK
import uk.gov.android.network.api.ApiResponse
import uk.gov.android.network.client.GenericHttpClient

class AttestationApiCallTest {
    private lateinit var context: Context
    private lateinit var httpClient: GenericHttpClient

    private lateinit var assertionApiCall: AttestationCaller

    @BeforeEach
    fun setUp() {
        context = mock()
        httpClient = mock()
        assertionApiCall = AttestationApiCall(context, httpClient)

        whenever(context.getString(any())).thenReturn("/endpoint")
        whenever(context.getString(any(), eq("/endpoint"))).thenAnswer { "www.testUrl.com" }
    }

    @Test
    fun `call() - Success`() =
        runBlocking {
            val expectedResult = AttestationResponse.Success("Success", 0)

            whenever(httpClient.makeRequest(any()))
                .thenReturn(ApiResponse.Success(VALID_CLIENT_ATTESTATION))

            val result =
                assertionApiCall.call(
                    "",
                    jwk
                )

            assertEquals(expectedResult, result)
        }

    @Test
    fun `call() - Failure with error message`() =
        runBlocking {
            val error = IOException("Test error message")
            val expectedResult = AttestationResponse.Failure(error.message!!, error = error)
            whenever(httpClient.makeRequest(any()))
                .thenReturn(ApiResponse.Failure(500, error))

            val result =
                assertionApiCall.call(
                    "",
                    jwk
                )

            assertEquals(expectedResult, result)
        }

    @Test
    fun `call() - Failure without error message`() =
        runBlocking {
            val error = IOException()
            val expectedResult =
                AttestationResponse
                    .Failure(AttestationApiCall.NETWORK_ERROR, error = error)
            whenever(httpClient.makeRequest(any()))
                .thenReturn(ApiResponse.Failure(500, error))

            val result =
                assertionApiCall.call(
                    "",
                    jwk
                )

            assertEquals(expectedResult, result)
        }

    @Test
    fun `call() - Failure when ApiResponse is Offline`() =
        runBlocking {
            val expectedResult =
                AttestationResponse
                    .Failure(AttestationApiCall.NETWORK_ERROR)
            whenever(httpClient.makeRequest(any()))
                .thenReturn(ApiResponse.Offline)

            val result =
                assertionApiCall.call(
                    "",
                    jwk
                )

            assertEquals(expectedResult, result)
        }

    @Test
    fun `call() - Json failure`() =
        runBlocking {
            whenever(httpClient.makeRequest(any()))
                .thenReturn(ApiResponse.Success(INVALID_CLIENT_ATTESTATION))

            val result = assertionApiCall.call("", jwk)

            assert((result as AttestationResponse.Failure).error!! is IllegalArgumentException)
        }

    companion object {
        private val jwk = JWK.generateJwk("x", "y")
        private const val INVALID_CLIENT_ATTESTATION =
            "{\"client_attestation\": \"Success\", " +
                "\"expires_in\": \"a\"}"
        private const val VALID_CLIENT_ATTESTATION =
            "{\"client_attestation\": \"Success\", " +
                "\"expires_in\": \"0\"}"
    }
}
