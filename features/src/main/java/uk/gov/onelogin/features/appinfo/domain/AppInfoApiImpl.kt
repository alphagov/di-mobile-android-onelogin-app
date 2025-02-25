package uk.gov.onelogin.features.appinfo.domain

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import uk.gov.android.network.api.ApiRequest
import uk.gov.android.network.api.ApiResponse
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.android.network.online.OnlineChecker
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.features.appinfo.data.model.AppInfoData

class AppInfoApiImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val httpClient: GenericHttpClient,
    private val onlineChecker: OnlineChecker
) : AppInfoApi {
    private val jsonDecoder = Json { ignoreUnknownKeys = true }

    override suspend fun callApi(): ApiResponse {
        if (!onlineChecker.isOnline()) {
            return ApiResponse.Offline
        }
        val endpoint = context.getString(R.string.appInfoEndpoint)
        val request = ApiRequest.Get(
            url = context.getString(R.string.appInfoUrl, endpoint),
            headers =
            listOf(
                "Cache-Control" to "no-store",
                "Content-Type" to "application/json",
                "Strict-Transport-Security" to "max-age=31536000",
                "X-Content-Type-Options" to "nosniff",
                "X-Frame-Options" to "DENY"
            )
        )
        return when (val response = httpClient.makeRequest(request)) {
            is ApiResponse.Success<*> ->
                try {
                    val decodedResponse = jsonDecoder
                        .decodeFromString<AppInfoData>(response.response.toString())
                    Log.d("AppInfoRemote", "$decodedResponse")
                    ApiResponse.Success(decodedResponse)
                } catch (e: SerializationException) {
                    ApiResponse.Failure(1, e)
                }
            else -> response
        }
    }
}
