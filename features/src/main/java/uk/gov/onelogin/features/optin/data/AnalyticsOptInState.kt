package uk.gov.onelogin.features.optin.data

enum class AnalyticsOptInState {
    None, Yes, No;

    val isUnset: Boolean
        get() = this == None
}
