package uk.gov.onelogin.features.developer.ui.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import uk.gov.android.authentication.login.TokenResponse
import uk.gov.onelogin.core.network.domain.HelloWorldApiCall
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.domain.retrieve.GetEmail

@HiltViewModel
class AuthTabScreenViewModel @Inject constructor(
    private val helloWorldApiCall: HelloWorldApiCall,
    private val tokenRepository: TokenRepository,
    getEmail: GetEmail
) : ViewModel() {
    private val _happyHelloWorldResponse = mutableStateOf("")
    val happyHelloWorldResponse: State<String>
        get() = _happyHelloWorldResponse

    private val _happyCallLoading = mutableStateOf(false)
    val happyCallLoading: State<Boolean>
        get() = _happyCallLoading

    private val _authFailingHelloWorldResponse = mutableStateOf("")
    val authFailingHelloWorldResponse: State<String>
        get() = _authFailingHelloWorldResponse

    private val _authFailingCallLoading = mutableStateOf(false)
    val authFailingCallLoading: State<Boolean>
        get() = _authFailingCallLoading

    private val _serviceFailingHelloWorldResponse = mutableStateOf("")
    val serviceFailingHelloWorldResponse: State<String>
        get() = _serviceFailingHelloWorldResponse

    private val _serviceFailingCallLoading = mutableStateOf(false)
    val serviceFailingCallLoading: State<Boolean>
        get() = _serviceFailingCallLoading

    val email = getEmail().orEmpty()

    fun getTokens(): TokenResponse? {
        return tokenRepository.getTokenResponse()
    }

    fun makeHappyHelloWorldCall() {
        _happyCallLoading.value = true
        viewModelScope.launch {
            _happyHelloWorldResponse.value = helloWorldApiCall.happyPath()
            _happyCallLoading.value = false
        }
    }

    fun makeAuthFailingHelloWorldCall() {
        _authFailingCallLoading.value = true
        viewModelScope.launch {
            val currentToken = tokenRepository.getTokenResponse()
            tokenRepository.clearTokenResponse()
            _authFailingHelloWorldResponse.value = helloWorldApiCall.happyPath()
            _authFailingCallLoading.value = false
            if (currentToken != null) {
                tokenRepository.setTokenResponse(currentToken)
            }
        }
    }

    fun makeServiceFailingHelloWorldCall() {
        _serviceFailingCallLoading.value = true
        viewModelScope.launch {
            _serviceFailingHelloWorldResponse.value = helloWorldApiCall.errorPath()
            _serviceFailingCallLoading.value = false
        }
    }
}
