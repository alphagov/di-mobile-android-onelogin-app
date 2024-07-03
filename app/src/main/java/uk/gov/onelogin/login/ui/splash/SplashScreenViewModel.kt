package uk.gov.onelogin.login.ui.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlinx.coroutines.launch
import uk.gov.onelogin.login.LoginRoutes
import uk.gov.onelogin.login.state.LocalAuthStatus
import uk.gov.onelogin.login.usecase.HandleLogin
import uk.gov.onelogin.mainnav.nav.MainNavRoutes

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val handleLogin: HandleLogin
) : ViewModel(), DefaultLifecycleObserver {

    private val timesResumed: AtomicInteger = AtomicInteger(0)
    private val _showUnlock = mutableStateOf(false)
    val showUnlock: State<Boolean> = _showUnlock

    private val _next = MutableLiveData<String>()
    val next: LiveData<String> = _next

    fun login(fragmentActivity: FragmentActivity, fromLockScreen: Boolean = false) {
        if (fromLockScreen && timesResumed.get() == 1) return
        viewModelScope.launch {
            handleLogin(
                fragmentActivity,
                callback = {
                    when (it) {
                        LocalAuthStatus.SecureStoreError -> _next.value = LoginRoutes.SIGN_IN_ERROR
                        LocalAuthStatus.ManualSignIn ->
                            _next.value = LoginRoutes.WELCOME

                        is LocalAuthStatus.Success ->
                            _next.value = MainNavRoutes.START

                        LocalAuthStatus.UserCancelled ->
                            _showUnlock.value = true

                        LocalAuthStatus.BioCheckFailed -> {
                            // Allow user to make multiple fails... do nothing for now
                        }
                    }
                }
            )
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        timesResumed.addAndGet(1)
        super.onResume(owner)
    }
}
