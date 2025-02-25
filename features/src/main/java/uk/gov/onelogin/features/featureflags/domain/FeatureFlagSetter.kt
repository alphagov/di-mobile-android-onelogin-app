package uk.gov.onelogin.features.featureflags.domain

import uk.gov.onelogin.features.appinfo.data.model.AppInfoData

fun interface FeatureFlagSetter {
    fun setFromAppInfo(appInfo: AppInfoData.AppInfo)
}
