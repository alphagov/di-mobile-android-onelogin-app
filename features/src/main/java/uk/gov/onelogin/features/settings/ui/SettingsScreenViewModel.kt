package uk.gov.onelogin.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.gov.onelogin.core.navigation.data.SignOutRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.domain.retrieve.GetEmail
import uk.gov.onelogin.features.optin.data.OptInRepository

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val optInRepository: OptInRepository,
    private val navigator: Navigator,
    getEmail: GetEmail
) : ViewModel() {
    private val _optInState = MutableStateFlow(false)
    val optInState: StateFlow<Boolean>
        get() = _optInState.asStateFlow()

    init {
        viewModelScope.launch {
            optInRepository.hasAnalyticsOptIn().collect {
                _optInState.value = it
            }
        }
    }

    val email = getEmail().orEmpty()

    fun goToSignOut() {
        navigator.navigate(SignOutRoutes.Start)
    }

    fun toggleOptInPreference() {
        viewModelScope.launch {
            _optInState.update { currentOptInState ->
                currentOptInState.not().also { newOptInState ->
                    when (newOptInState) {
                        true -> optInRepository.optIn()
                        false -> optInRepository.optOut()
                    }
                }
            }
        }
    }
}
