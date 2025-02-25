package uk.gov.onelogin.core.biometrics.domain

import android.content.Context
import androidx.biometric.BiometricManager as AndroidBiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import uk.gov.onelogin.core.biometrics.data.BiometricStatus

class BiometricManagerImpl @Inject constructor(
    @ApplicationContext
    context: Context
) : BiometricManager {
    private val androidBiometricManager = AndroidBiometricManager.from(context)

    override fun canAuthenticate() =
        BiometricStatus.forAndroidInt(
            androidBiometricManager.canAuthenticate(BIOMETRIC_STRONG)
        )
}
