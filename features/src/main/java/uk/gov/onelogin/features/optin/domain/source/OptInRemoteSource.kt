package uk.gov.onelogin.features.optin.domain.source

import uk.gov.onelogin.features.optin.data.AnalyticsOptInState

fun interface OptInRemoteSource {
    suspend fun update(state: AnalyticsOptInState)
}
