package uk.gov.onelogin.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uk.gov.android.features.FeatureFlags
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.onelogin.features.CriOrchestratorFeatureFlag
import uk.gov.onelogin.navigation.Navigator

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val httpClient: GenericHttpClient,
    val analyticsLogger: AnalyticsLogger,
    private val featureFlag: FeatureFlags,
    private val navigator: Navigator
) : ViewModel() {
    private val _uiCardEnabled = MutableStateFlow(featureFlag[CriOrchestratorFeatureFlag.ENABLED])
    val uiCardEnabled: StateFlow<Boolean> = _uiCardEnabled

    fun openDevPanel() {
        navigator.openDeveloperPanel()
    }

    fun getUiCardFlagState() {
        _uiCardEnabled.value = featureFlag[CriOrchestratorFeatureFlag.ENABLED]
    }
}
