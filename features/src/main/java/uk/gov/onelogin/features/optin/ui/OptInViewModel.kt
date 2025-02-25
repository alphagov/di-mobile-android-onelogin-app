package uk.gov.onelogin.features.optin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.features.optin.data.OptInRepository
import uk.gov.onelogin.features.optin.domain.SingleChoice

@HiltViewModel
class OptInViewModel @Inject constructor(
    private val repository: OptInRepository,
    private val navigator: Navigator
) : ViewModel() {
    private val choice = SingleChoice()
    val uiState: Flow<OptInUIState> = choice.state.map {
        when (it) {
            SingleChoice.State.PreChoice -> OptInUIState.PreChoice
            SingleChoice.State.PostChoice -> OptInUIState.PostChoice
        }
    }

    fun optIn() {
        choice.choose {
            viewModelScope.launch {
                repository.optIn()
            }
        }
    }

    fun optOut() {
        choice.choose {
            viewModelScope.launch {
                repository.optOut()
            }
        }
    }

    fun goToSignIn() {
        navigator.navigate(LoginRoutes.Welcome, true)
    }
}
