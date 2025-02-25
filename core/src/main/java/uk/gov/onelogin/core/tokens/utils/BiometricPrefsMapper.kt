package uk.gov.onelogin.core.tokens.utils

import uk.gov.android.securestore.AccessControlLevel
import uk.gov.onelogin.core.biometrics.data.BiometricPreference

object BiometricPrefsMapper {
    /**
     * BiometricPrefsMapper method to map [BiometricPreference] options onto [AccessControlLevel] options. Allows user choice to be used by the secure store
     *
     * @param bioPref [BiometricPreference] set by user
     * @return [AccessControlLevel] that the given [BiometricPreference] maps to for use in SecureStore instance
     */
    fun mapAccessControlLevel(bioPref: BiometricPreference): AccessControlLevel =
        when (bioPref) {
            BiometricPreference.BIOMETRICS -> AccessControlLevel.PASSCODE_AND_CURRENT_BIOMETRICS
            BiometricPreference.PASSCODE -> AccessControlLevel.PASSCODE
            BiometricPreference.NONE -> AccessControlLevel.OPEN
        }
}
