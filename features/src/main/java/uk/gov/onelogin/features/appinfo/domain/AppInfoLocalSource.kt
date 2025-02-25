package uk.gov.onelogin.features.appinfo.domain

import uk.gov.onelogin.features.appinfo.data.model.AppInfoData
import uk.gov.onelogin.features.appinfo.data.model.AppInfoLocalState

interface AppInfoLocalSource {
    fun get(): AppInfoLocalState

    fun update(value: AppInfoData)
}
