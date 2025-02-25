package uk.gov.onelogin.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.Dispatchers
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.cleaner.domain.Cleaner
import uk.gov.onelogin.core.cleaner.domain.MultiCleaner
import uk.gov.onelogin.core.tokens.domain.remove.RemoveAllSecureStoreData
import uk.gov.onelogin.core.tokens.domain.remove.RemoveTokenExpiry
import uk.gov.onelogin.features.optin.data.OptInRepository

@Module
@InstallIn(ViewModelComponent::class)
internal object CleanerModule {
    @Provides
    fun provideCleaner(
        optInRepository: OptInRepository,
        biometricPreferenceHandler: BiometricPreferenceHandler,
        secureStoreData: RemoveAllSecureStoreData,
        removeTokenExpiry: RemoveTokenExpiry
    ): Cleaner = MultiCleaner(
        Dispatchers.Default,
        removeTokenExpiry,
        optInRepository,
        biometricPreferenceHandler,
        secureStoreData
    )
}
