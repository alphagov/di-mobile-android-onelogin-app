package uk.gov.onelogin.core.tokens.data.initialise

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uk.gov.android.securestore.SecureStorageConfiguration
import uk.gov.android.securestore.SecureStore
import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.tokens.domain.save.SaveTokens
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys
import uk.gov.onelogin.core.tokens.utils.BiometricPrefsMapper
import uk.gov.onelogin.core.tokens.utils.DefaultDispatcher

class AutoInitialiseSecureStoreImpl @Inject constructor(
    private val biometricPreferenceHandler: BiometricPreferenceHandler,
    private val saveTokens: SaveTokens,
    @Named("Token")
    private val secureStore: SecureStore,
    @ApplicationContext
    private val context: Context,
    @DefaultDispatcher
    private val coroutineContext: CoroutineDispatcher
) : AutoInitialiseSecureStore {
    override suspend fun initialise() {
        val bioPref = biometricPreferenceHandler.getBioPref()
        if (bioPref == null || bioPref == BiometricPreference.NONE) return
        val configuration = SecureStorageConfiguration(
            AuthTokenStoreKeys.TOKEN_SECURE_STORE_ID,
            BiometricPrefsMapper.mapAccessControlLevel(bioPref)
        )
        withContext(coroutineContext) {
            secureStore.init(context, configuration)
            saveTokens()
        }
    }
}
