package uk.gov.onelogin.features.developer.ui.appintegrity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.util.date.getTimeMillis
import javax.inject.Inject
import kotlinx.coroutines.launch
import uk.gov.android.authentication.integrity.AppIntegrityManager
import uk.gov.android.authentication.integrity.appcheck.usecase.AppChecker
import uk.gov.onelogin.core.tokens.domain.retrieve.GetFromOpenSecureStore
import uk.gov.onelogin.core.tokens.domain.save.SaveToOpenSecureStore
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrity
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrity.Companion.CLIENT_ATTESTATION
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrity.Companion.CLIENT_ATTESTATION_EXPIRY

@HiltViewModel
class AppIntegrityTabViewModel @Inject constructor(
    private val firebaseAppCheck: AppChecker,
    private val appCheck: AppIntegrityManager,
    private val appIntegrity: AppIntegrity,
    private val getFromOpenSecureStore: GetFromOpenSecureStore,
    private val saveToOpenSecureStore: SaveToOpenSecureStore
) : ViewModel() {
    val tokenResponse: MutableState<String> = mutableStateOf("")
    val networkResponse: MutableState<String> = mutableStateOf("")
    val clientAttestationResult: MutableState<String> = mutableStateOf("")
    val clientAttestation: MutableState<String> = mutableStateOf("")
    val clientAttestationExpiry: MutableState<String> = mutableStateOf("")
    val proofOfPossessionResult: MutableState<String> = mutableStateOf("")

    init {
        getSavedAttestation()
    }

    fun getToken() {
        viewModelScope.launch {
            tokenResponse.value = "Loading..."
            tokenResponse.value = firebaseAppCheck.getAppCheckToken().toString()
        }
    }

    fun makeNetworkCall() {
        viewModelScope.launch {
            networkResponse.value = "Loading..."
            networkResponse.value = appCheck.getAttestation().toString()
            getSavedAttestation()
        }
    }

    fun getClientAttestation() {
        viewModelScope.launch {
            clientAttestationResult.value = "Loading..."
            clientAttestationResult.value = appIntegrity.getClientAttestation().toString()
            getSavedAttestation()
        }
    }

    fun generatePoP() {
        proofOfPossessionResult.value = "Loading..."
        proofOfPossessionResult.value = appIntegrity.getProofOfPossession().toString()
    }

    fun resetAttestation() {
        viewModelScope.launch {
            saveToOpenSecureStore.save(CLIENT_ATTESTATION, "")
            getSavedAttestation()
        }
    }

    fun setFakeAttestation() {
        viewModelScope.launch {
            saveToOpenSecureStore.save(CLIENT_ATTESTATION, "fake attestation")
            getSavedAttestation()
        }
    }

    fun resetAttestationExpiry() {
        viewModelScope.launch {
            saveToOpenSecureStore.save(CLIENT_ATTESTATION_EXPIRY, "")
            getSavedAttestation()
        }
    }

    fun setFutureAttestationExpiry() {
        viewModelScope.launch {
            val time = getTimeMillis() + TIME_TO_ADD
            saveToOpenSecureStore.save(CLIENT_ATTESTATION_EXPIRY, time.toString())
            getSavedAttestation()
        }
    }

    private fun getSavedAttestation() {
        viewModelScope.launch {
            clientAttestation.value = getFromOpenSecureStore.invoke(CLIENT_ATTESTATION).toString()
            clientAttestationExpiry.value =
                getFromOpenSecureStore.invoke(CLIENT_ATTESTATION_EXPIRY).toString()
        }
    }

    companion object {
        private const val TIME_TO_ADD = 50000
    }
}
