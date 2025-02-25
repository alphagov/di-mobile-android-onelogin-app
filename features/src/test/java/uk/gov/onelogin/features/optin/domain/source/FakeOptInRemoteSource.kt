package uk.gov.onelogin.features.optin.domain.source

import uk.gov.onelogin.features.optin.data.AnalyticsOptInState

class FakeOptInRemoteSource : OptInRemoteSource {
    var currentState = AnalyticsOptInState.None

    override suspend fun update(state: AnalyticsOptInState) {
        currentState = state
    }
}
