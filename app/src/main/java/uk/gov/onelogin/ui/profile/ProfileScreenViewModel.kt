package uk.gov.onelogin.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.gov.onelogin.navigation.Navigator
import uk.gov.onelogin.optin.domain.repository.OptInRepository
import uk.gov.onelogin.signOut.SignOutRoutes
import uk.gov.onelogin.tokens.usecases.GetEmail

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
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

    fun toggleOptInPreference(optIn: Boolean) {
        viewModelScope.launch {
            _optInState.value = !optIn
            if (optIn) {
                optInRepository.optOut()
            } else {
                optInRepository.optIn()
            }
        }
    }
}
