package uk.gov.onelogin.credentialchecker

/**
 * Interface to mirror [BiometricManager] from Android, simply checks the OS for
 * the status of biometric hardware on the device
 */
fun interface BiometricManager {

    /**
     * Checks (strong) biometric hardware status on the device
     *
     * @return [BiometricStatus]
     */
    fun canAuthenticate(): BiometricStatus
}
