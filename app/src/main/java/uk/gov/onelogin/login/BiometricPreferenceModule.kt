package uk.gov.onelogin.login

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandlerImpl

@Module
@InstallIn(SingletonComponent::class)
fun interface BiometricPreferenceModule {
    @Binds
    @Singleton
    fun bindBiometricPreferenceHandler(
        handler: BiometricPreferenceHandlerImpl
    ): BiometricPreferenceHandler
}
