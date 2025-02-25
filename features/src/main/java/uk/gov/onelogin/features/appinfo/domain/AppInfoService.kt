package uk.gov.onelogin.features.appinfo.domain

import uk.gov.onelogin.features.appinfo.data.model.AppInfoServiceState

fun interface AppInfoService {
    suspend fun get(): AppInfoServiceState
}
