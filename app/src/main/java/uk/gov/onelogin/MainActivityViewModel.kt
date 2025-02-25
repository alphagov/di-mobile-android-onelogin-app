package uk.gov.onelogin

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.data.initialise.AutoInitialiseSecureStore
import uk.gov.onelogin.features.optin.data.AnalyticsOptInRepository

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val analyticsOptInRepo: AnalyticsOptInRepository,
    private val bioPrefHandler: BiometricPreferenceHandler,
    private val tokenRepository: TokenRepository,
    private val navigator: Navigator,
    autoInitialiseSecureStore: AutoInitialiseSecureStore
) : ViewModel(), DefaultLifecycleObserver {

    init {
        viewModelScope.launch {
            autoInitialiseSecureStore.initialise()
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        viewModelScope.launch {
            analyticsOptInRepo.synchronise()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (bioPrefHandler.getBioPref() != BiometricPreference.NONE &&
            tokenRepository.getTokenResponse() != null
        ) {
            tokenRepository.clearTokenResponse()
            navigator.navigate(LoginRoutes.Start)
        }
    }
}
