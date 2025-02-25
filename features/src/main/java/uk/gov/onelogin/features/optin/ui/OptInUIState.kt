package uk.gov.onelogin.features.optin.ui

enum class OptInUIState(val hasButtonsOn: Boolean) {
    PreChoice(true),
    PostChoice(false)
}
