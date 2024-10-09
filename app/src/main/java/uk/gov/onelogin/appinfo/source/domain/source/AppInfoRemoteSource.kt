package uk.gov.onelogin.appinfo.source.domain.source

import uk.gov.onelogin.appinfo.service.domain.model.AppInfoServiceState
import uk.gov.onelogin.appinfo.source.domain.model.AppInfoRemoteState

fun interface AppInfoRemoteSource {
    suspend fun get(): AppInfoRemoteState
}
