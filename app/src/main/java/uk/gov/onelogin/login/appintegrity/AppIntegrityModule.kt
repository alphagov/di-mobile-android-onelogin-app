package uk.gov.onelogin.login.appintegrity

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlin.io.encoding.ExperimentalEncodingApi
import uk.gov.android.authentication.integrity.AppIntegrityManager
import uk.gov.android.authentication.integrity.FirebaseAppIntegrityManager
import uk.gov.android.authentication.integrity.appcheck.usecase.AppChecker
import uk.gov.android.authentication.integrity.appcheck.usecase.AttestationCaller
import uk.gov.android.authentication.integrity.keymanager.ECKeyManager
import uk.gov.android.authentication.integrity.keymanager.KeyStoreManager
import uk.gov.android.authentication.integrity.model.AppIntegrityConfiguration
import uk.gov.android.featureflags.FeatureFlags
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.onelogin.core.tokens.domain.retrieve.GetFromOpenSecureStore
import uk.gov.onelogin.core.tokens.domain.save.SaveToOpenSecureStore
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrity
import uk.gov.onelogin.features.login.domain.appintegrity.AppIntegrityImpl
import uk.gov.onelogin.features.login.domain.appintegrity.AttestationApiCall

@SuppressWarnings("kotlin:S6517")
@Module
@InstallIn(ViewModelComponent::class)
object AppIntegrityModule {
    @Provides
    fun provideAppIntegrityConfig(
        attestationCaller: AttestationCaller,
        appChecker: AppChecker,
        keyStoreManager: KeyStoreManager
    ): AppIntegrityConfiguration {
        return AppIntegrityConfiguration(
            attestationCaller = attestationCaller,
            appChecker = appChecker,
            keyStoreManager = keyStoreManager
        )
    }

    @Provides
    fun provideFirebaseTokenManager(
        config: AppIntegrityConfiguration
    ): AppIntegrityManager {
        return FirebaseAppIntegrityManager(config)
    }

    @Provides
    fun provideAppIntegrityCheck(
        @ApplicationContext
        context: Context,
        featureFlags: FeatureFlags,
        appCheck: AppIntegrityManager,
        saveToOpenSecureStore: SaveToOpenSecureStore,
        getFromOpenSecureStore: GetFromOpenSecureStore
    ): AppIntegrity {
        return AppIntegrityImpl(
            context,
            featureFlags,
            appCheck,
            saveToOpenSecureStore,
            getFromOpenSecureStore
        )
    }

    @Provides
    fun provideAssertionApiCall(
        @ApplicationContext
        context: Context,
        genericHttpClient: GenericHttpClient
    ): AttestationCaller = AttestationApiCall(context, genericHttpClient)

    @OptIn(ExperimentalEncodingApi::class)
    @Provides
    fun provideECKeyManager(): KeyStoreManager = ECKeyManager()
}
