package uk.gov.onelogin.biometrics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import uk.gov.onelogin.core.biometrics.domain.BiometricManager
import uk.gov.onelogin.core.biometrics.domain.BiometricManagerImpl
import uk.gov.onelogin.core.biometrics.domain.CredentialChecker
import uk.gov.onelogin.core.biometrics.domain.DeviceCredentialChecker

@Module
@InstallIn(ViewModelComponent::class)
interface DeviceCredentialCheckerModule {
    @Binds
    @ViewModelScoped
    fun bindCredentialChecker(checker: DeviceCredentialChecker): CredentialChecker

    @Binds
    @ViewModelScoped
    fun bindBiometricManager(manager: BiometricManagerImpl): BiometricManager
}
