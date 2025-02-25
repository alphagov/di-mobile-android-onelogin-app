package uk.gov.onelogin.features.login.domain.appintegrity

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.serialization.json.Json
import uk.gov.android.authentication.integrity.appcheck.model.AttestationResponse
import uk.gov.android.authentication.integrity.appcheck.usecase.AttestationCaller
import uk.gov.android.authentication.json.jwk.JWK
import uk.gov.android.network.api.ApiRequest
import uk.gov.android.network.api.ApiResponse
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.android.onelogin.core.R

class AttestationApiCall @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val httpClient: GenericHttpClient
) : AttestationCaller {
    override suspend fun call(
        token: String,
        jwk: JWK.JsonWebKey
    ): AttestationResponse {
        val endpoint = context.getString(R.string.clientAttestationEndpoint)
        val request = ApiRequest.Post(
            url = context.getString(R.string.webBaseUrl, endpoint) + "?device=android",
            body = jwk,
            headers = listOf(
                AttestationCaller.FIREBASE_HEADER to token,
                AttestationCaller.CONTENT_TYPE to AttestationCaller.CONTENT_TYPE_VALUE
            )
        )
        return when (val apiResponse = httpClient.makeRequest(request)) {
            is ApiResponse.Success<*> -> handleResponse(apiResponse)
            is ApiResponse.Failure ->
                AttestationResponse.Failure(
                    apiResponse.error.message ?: NETWORK_ERROR,
                    apiResponse.error
                )

            else -> AttestationResponse.Failure(NETWORK_ERROR)
        }
    }

    private fun handleResponse(apiResponse: ApiResponse) =
        try {
            val response = (apiResponse as ApiResponse.Success<String>).response
            Json.decodeFromString<AttestationResponse.Success>(response)
        } catch (e: IllegalArgumentException) {
            AttestationResponse.Failure(
                e.message ?: JSON_DECODE_ERROR,
                e
            )
        }

    companion object {
        const val NETWORK_ERROR = "Network error"
        const val JSON_DECODE_ERROR = "ERROR: Decode AttestationResponse.Success error"
    }
}
