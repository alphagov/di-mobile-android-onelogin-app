package uk.gov.onelogin.features.login.ui.signin.welcome

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.gov.android.authentication.login.TokenResponse
import uk.gov.android.network.online.OnlineChecker
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.biometrics.data.BiometricStatus
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.biometrics.domain.CredentialChecker
import uk.gov.onelogin.core.navigation.data.ErrorRoutes
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.data.MainNavRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.data.initialise.AutoInitialiseSecureStore
import uk.gov.onelogin.core.tokens.domain.VerifyIdToken
import uk.gov.onelogin.core.tokens.domain.save.SaveTokenExpiry
import uk.gov.onelogin.core.tokens.domain.save.SaveTokens
import uk.gov.onelogin.features.login.domain.signin.loginredirect.HandleLoginRedirect
import uk.gov.onelogin.features.login.domain.signin.remotelogin.HandleRemoteLogin

@HiltViewModel
@Suppress("LongParameterList")
class WelcomeScreenViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val credChecker: CredentialChecker,
    private val bioPrefHandler: BiometricPreferenceHandler,
    private val tokenRepository: TokenRepository,
    private val autoInitialiseSecureStore: AutoInitialiseSecureStore,
    private val verifyIdToken: VerifyIdToken,
    private val navigator: Navigator,
    private val saveTokens: SaveTokens,
    private val saveTokenExpiry: SaveTokenExpiry,
    private val handleRemoteLogin: HandleRemoteLogin,
    private val handleLoginRedirect: HandleLoginRedirect,
    val onlineChecker: OnlineChecker
) : ViewModel() {
    private val tag = this::class.java.simpleName
    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun onPrimary(launcher: ActivityResultLauncher<Intent>) =
        viewModelScope.launch {
            _loading.emit(true)
            handleRemoteLogin.login(
                launcher
            ) {
                navigator.navigate(LoginRoutes.SignInError)
            }
        }

    fun handleActivityResult(
        intent: Intent,
        isReAuth: Boolean = false
    ) {
        if (intent.data == null) return

        viewModelScope.launch {
            _loading.emit(true)
            handleLoginRedirect.handle(
                intent,
                onSuccess = {
                    viewModelScope.launch {
                        handleTokens(it, isReAuth)
                    }
                },
                onFailure = {
                    Log.e(tag, it?.message, it)
                    navigator.navigate(LoginRoutes.SignInError, true)
                }
            )
        }
    }

    fun navigateToDevPanel() {
        navigator.openDeveloperPanel()
    }

    fun navigateToOfflineError() {
        navigator.navigate(ErrorRoutes.Offline)
    }

    fun stopLoading() {
        _loading.value = false
    }

    private suspend fun handleTokens(
        tokens: TokenResponse,
        isReAuth: Boolean
    ) {
        val jwksUrl = context.getString(
            R.string.stsUrl,
            context.getString(R.string.jwksEndpoint)
        )

        if (!verifyIdToken(tokens.idToken, jwksUrl)) {
            navigator.navigate(LoginRoutes.SignInError, true)
        } else {
            checkLocalAuthRoute(tokens, isReAuth)
        }
    }

    private suspend fun checkLocalAuthRoute(
        tokens: TokenResponse,
        isReAuth: Boolean
    ) {
        tokenRepository.setTokenResponse(tokens)
        saveTokenExpiry(tokens.accessTokenExpirationTime)

        when {
            isReAuth -> {
                if (credChecker.isDeviceSecure()) {
                    saveTokens()
                }
                navigator.goBack()
            }

            !credChecker.isDeviceSecure() -> {
                bioPrefHandler.setBioPref(BiometricPreference.NONE)
                navigator.navigate(MainNavRoutes.Start, true)
            }

            shouldSeeBiometricOptIn() ->
                navigator.navigate(LoginRoutes.BioOptIn, true)

            else -> {
                if (bioPrefHandler.getBioPref() != BiometricPreference.BIOMETRICS) {
                    bioPrefHandler.setBioPref(BiometricPreference.PASSCODE)
                }
                viewModelScope.launch {
                    autoInitialiseSecureStore.initialise()
                }
                navigator.navigate(MainNavRoutes.Start, true)
            }
        }
    }

    private fun shouldSeeBiometricOptIn() =
        credChecker.biometricStatus() == BiometricStatus.SUCCESS &&
            (
                bioPrefHandler.getBioPref() == null ||
                    bioPrefHandler.getBioPref() == BiometricPreference.NONE
                )
}
