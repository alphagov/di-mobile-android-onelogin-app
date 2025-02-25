package uk.gov.onelogin.features.optin.domain.source

import uk.gov.onelogin.features.optin.data.AnalyticsOptInState

class FakeOptInLocalSource : OptInLocalSource {
    var currentState = AnalyticsOptInState.None

    override suspend fun getState(): AnalyticsOptInState {
        return currentState
    }

    override suspend fun update(state: AnalyticsOptInState) {
        currentState = state
    }
}
