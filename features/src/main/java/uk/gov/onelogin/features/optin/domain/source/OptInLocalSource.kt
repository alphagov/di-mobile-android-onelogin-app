package uk.gov.onelogin.features.optin.domain.source

import uk.gov.onelogin.features.optin.data.AnalyticsOptInState

interface OptInLocalSource {
    suspend fun getState(): AnalyticsOptInState

    suspend fun update(state: AnalyticsOptInState)
}
