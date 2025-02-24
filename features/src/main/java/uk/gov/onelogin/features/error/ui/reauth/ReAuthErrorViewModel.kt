package uk.gov.onelogin.features.error.ui.reauth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator

@HiltViewModel
class ReAuthErrorViewModel @Inject constructor(
    private val navigator: Navigator
) : ViewModel() {
    fun navigateToSignIn() {
        navigator.navigate(LoginRoutes.Start, true)
    }
}
