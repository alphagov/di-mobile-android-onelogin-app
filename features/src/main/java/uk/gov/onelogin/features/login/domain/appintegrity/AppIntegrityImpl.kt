package uk.gov.onelogin.features.login.domain.appintegrity

import android.content.Context
import io.ktor.util.date.getTimeMillis
import javax.inject.Inject
import uk.gov.android.authentication.integrity.AppIntegrityManager
import uk.gov.android.authentication.integrity.appcheck.model.AttestationResponse
import uk.gov.android.authentication.integrity.pop.SignedPoP
import uk.gov.android.featureflags.FeatureFlags
import uk.gov.android.onelogin.core.R
import uk.gov.android.securestore.error.SecureStorageError
import uk.gov.onelogin.core.tokens.domain.retrieve.GetFromOpenSecureStore
import uk.gov.onelogin.core.tokens.domain.save.SaveToOpenSecureStore
import uk.gov.onelogin.features.featureflags.data.AppIntegrityFeatureFlag
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrity.Companion.CLIENT_ATTESTATION
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrity.Companion.CLIENT_ATTESTATION_EXPIRY
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrity.Companion.SECURE_STORE_ERROR

class AppIntegrityImpl @Inject constructor(
    private val context: Context,
    private val featureFlags: FeatureFlags,
    private val appCheck: AppIntegrityManager,
    private val saveToOpenSecureStore: SaveToOpenSecureStore,
    private val getFromOpenSecureStore: GetFromOpenSecureStore
) : AppIntegrity {
    override suspend fun getClientAttestation(): AttestationResult {
        return if (isAttestationCallRequired()) {
            val result = appCheck.getAttestation()
            when (result) {
                is AttestationResponse.Success -> handleClientAttestation(result)
                is AttestationResponse.Failure -> AttestationResult.Failure(result.reason)
            }
        } else {
            AttestationResult.NotRequired(retrieveSavedClientAttestation())
        }
    }

    override fun getProofOfPossession(): SignedPoP {
        return appCheck.generatePoP(
            iss = context.getString(R.string.stsClientId),
            aud = context.getString(R.string.baseStsUrl)
        )
    }

    override suspend fun retrieveSavedClientAttestation(): String? {
        return getFromOpenSecureStore.invoke(CLIENT_ATTESTATION)?.get(CLIENT_ATTESTATION)
    }

    private suspend fun handleClientAttestation(result: AttestationResponse.Success) =
        try {
            saveToOpenSecureStore.save(CLIENT_ATTESTATION, result.attestationJwt)
            appCheck.getExpiry(result.attestationJwt)?.let {
                saveToOpenSecureStore.save(CLIENT_ATTESTATION_EXPIRY, it)
            }
            AttestationResult.Success(result.attestationJwt)
        } catch (e: SecureStorageError) {
            AttestationResult.Failure(e.message ?: SECURE_STORE_ERROR)
        }

    private suspend fun isAttestationCallRequired(): Boolean {
        val ssResult: Map<String, String>? = getFromOpenSecureStore.invoke(
            CLIENT_ATTESTATION_EXPIRY,
            CLIENT_ATTESTATION
        )
        val exp = ssResult?.get(CLIENT_ATTESTATION_EXPIRY)
        val attestation = ssResult?.get(CLIENT_ATTESTATION)

        val result = isAttestationExpired(exp) || attestation.isNullOrEmpty() ||
            !appCheck.verifyAttestationJwk(attestation)

        return featureFlags[AppIntegrityFeatureFlag.ENABLED] && result
    }

    private fun isAttestationExpired(expiryTime: String?): Boolean {
        return if (!expiryTime.isNullOrEmpty()) {
            expiryTime.toLong() <= getTimeMillis() / CONVERT_TO_SECONDS
        } else {
            true
        }
    }

    companion object {
        private const val CONVERT_TO_SECONDS = 1000
    }
}
