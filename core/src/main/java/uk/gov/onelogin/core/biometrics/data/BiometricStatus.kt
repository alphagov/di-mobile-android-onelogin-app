package uk.gov.onelogin.core.biometrics.data

import androidx.biometric.BiometricManager

enum class BiometricStatus {
    SUCCESS,
    NO_HARDWARE,
    HARDWARE_UNAVAILABLE,
    NOT_ENROLLED,
    UNKNOWN
    ;

    companion object {
        fun forAndroidInt(statusCode: Int): BiometricStatus =
            when (statusCode) {
                BiometricManager.BIOMETRIC_SUCCESS -> SUCCESS
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> NO_HARDWARE
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> HARDWARE_UNAVAILABLE
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> NOT_ENROLLED
                else -> UNKNOWN
            }
    }
}
