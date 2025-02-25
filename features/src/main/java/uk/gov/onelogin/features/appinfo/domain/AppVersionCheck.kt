package uk.gov.onelogin.features.appinfo.domain

import uk.gov.onelogin.features.appinfo.data.model.AppInfoData
import uk.gov.onelogin.features.appinfo.data.model.AppInfoServiceState

fun interface AppVersionCheck {
    fun compareVersions(data: AppInfoData): AppInfoServiceState
}

annotation class BuildConfigVersion
