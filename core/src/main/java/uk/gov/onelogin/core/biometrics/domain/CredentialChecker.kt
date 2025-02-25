package uk.gov.onelogin.core.biometrics.domain

import uk.gov.onelogin.core.biometrics.data.BiometricStatus

interface CredentialChecker {
    fun isDeviceSecure(): Boolean

    fun biometricStatus(): BiometricStatus
}
