package uk.gov.onelogin.core.network.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import uk.gov.android.network.api.ApiRequest
import uk.gov.android.network.api.ApiResponse
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.android.onelogin.core.R

class HelloWorldApiCallImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val httpClient: GenericHttpClient
) : HelloWorldApiCall {
    override suspend fun happyPath(): String {
        val endpoint = context.getString(R.string.helloWorldEndpoint)
        val request = ApiRequest.Get(
            url = context.getString(R.string.helloWorldUrl, endpoint)
        )
        val response = httpClient.makeAuthorisedRequest(request, "sts-test.hello-world.read")
        return handleResponse(response)
    }

    override suspend fun errorPath(): String {
        val endpoint = context.getString(R.string.helloWorldEndpoint) + "/error"
        val request = ApiRequest.Get(
            url = context.getString(R.string.helloWorldUrl, endpoint)
        )
        val response = httpClient.makeAuthorisedRequest(request, "sts-test.hello-world.read")

        return handleResponse(response)
    }

    private fun handleResponse(response: ApiResponse) = when (response) {
        is ApiResponse.Failure -> response.error.message ?: "Error"
        ApiResponse.Loading, ApiResponse.Offline -> "Error"
        is ApiResponse.Success<*> -> response.response.toString()
    }
}
