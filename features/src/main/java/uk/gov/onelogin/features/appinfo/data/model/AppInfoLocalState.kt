package uk.gov.onelogin.features.appinfo.data.model

sealed class AppInfoLocalState {
    data class Success(val value: AppInfoData) : AppInfoLocalState()

    data class Failure(val reason: String, val exp: Exception? = null) : AppInfoLocalState()
}
