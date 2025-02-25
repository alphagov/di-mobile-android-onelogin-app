package uk.gov.onelogin.features.appinfo.domain

import uk.gov.onelogin.features.appinfo.data.model.AppInfoRemoteState

fun interface AppInfoRemoteSource {
    suspend fun get(): AppInfoRemoteState
}
