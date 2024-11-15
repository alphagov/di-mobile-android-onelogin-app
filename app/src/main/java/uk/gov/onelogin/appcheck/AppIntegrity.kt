package uk.gov.onelogin.appcheck

import uk.gov.android.authentication.integrity.pop.SignedPoP

interface AppIntegrity {
    suspend fun getClientAttestation(): AttestationResult
    fun getProofOfPossession(): SignedPoP

    companion object {
        const val CLIENT_ATTESTATION = "appCheckClientAttestation"
        const val CLIENT_ATTESTATION_EXPIRY = "appCheckClientAttestationExpiry"
        const val SECURE_STORE_ERROR = "ERROR: Saving to open secure store error"
    }
}
