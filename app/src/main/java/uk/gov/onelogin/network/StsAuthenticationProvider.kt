package uk.gov.onelogin.network

import android.content.Context
import android.util.Log
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.Parameters
import kotlinx.serialization.json.Json
import uk.gov.android.network.api.ApiRequest
import uk.gov.android.network.api.ApiResponse
import uk.gov.android.network.auth.AuthenticationProvider
import uk.gov.android.network.auth.AuthenticationResponse
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.android.onelogin.R
import uk.gov.onelogin.repositiories.TokenRepository

class StsAuthenticationProvider(
    private val context: Context,
    private val tokenRepository: TokenRepository,
    private val httpClient: GenericHttpClient
) : AuthenticationProvider {
    override suspend fun fetchBearerToken(scope: String): AuthenticationResponse {
        val accessToken = tokenRepository.getTokenResponse()?.accessToken

        accessToken?.let { accessToken ->
            val request = ApiRequest.Post(
                url = context.getString(R.string.stsUrl),
                body = FormDataContent(
                    Parameters.build {
                        append(GRANT_TYPE, "urn:ietf:params:oauth:grant-type:token-exchange")
                        append(SUBJECT_TOKEN, accessToken)
                        append(SUBJECT_TOKEN_TYPE, "urn:ietf:params:oauth:token-type:access_token")
                        append(SCOPE, scope)
                    }
                ),
                headers = listOf(
                    Pair("Content-Type", "application/x-www-form-urlencoded")
                )
            )

            val response = httpClient.makeRequest(request)
            if (response is ApiResponse.Success<*>) {
                try {
                    val tokenResponseString = (response as ApiResponse.Success<String>).response
                    val tokenResponse: TokenResponse = Json.decodeFromString(tokenResponseString)
                    return AuthenticationResponse.Success(tokenResponse.token)
                } catch (e: Exception) {
                    Log.e(this::class.java.simpleName, e.message, e)
                    return AuthenticationResponse.Failure(e)
                }
            } else {
                return AuthenticationResponse.Failure(Exception("Failed to fetch service token"))
            }
        } ?: return AuthenticationResponse.Failure(Exception("No access token"))
    }

    companion object {
        private const val GRANT_TYPE = "grant_type"
        private const val SUBJECT_TOKEN = "subject_token"
        private const val SUBJECT_TOKEN_TYPE = "subject_token_type"
        private const val SCOPE = "scope"
    }
}
