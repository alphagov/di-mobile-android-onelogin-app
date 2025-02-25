package uk.gov.onelogin.core.tokens.data.initialise

import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

fun interface AutoInitialiseSecureStore {
    /**
     * Initialise the token secure store using id [AuthTokenStoreKeys.TOKEN_SECURE_STORE_ID].
     * The initialisation of the secure store only happens if BiometricPreference is set
     * to either [BiometricPreference.PASSCODE] or [BiometricPreference.BIOMETRICS].
     *
     * Also attempts to save any tokens in the [uk.gov.onelogin.repositiories.TokenRepository]
     */
    suspend fun initialise()
}
