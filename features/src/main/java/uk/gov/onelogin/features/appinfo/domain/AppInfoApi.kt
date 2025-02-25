package uk.gov.onelogin.features.appinfo.domain

import uk.gov.android.network.api.ApiResponse

fun interface AppInfoApi {
    suspend fun callApi(): ApiResponse
}
