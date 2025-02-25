package uk.gov.onelogin.core.tokens.domain.save

import android.util.Log
import javax.inject.Inject
import javax.inject.Named
import uk.gov.android.securestore.SecureStore
import uk.gov.android.securestore.error.SecureStorageError

class SaveToEncryptedSecureStoreImpl @Inject constructor(
    @Named("Token")
    private val secureStore: SecureStore
) : SaveToEncryptedSecureStore {
    override suspend fun invoke(
        key: String,
        value: String
    ) {
        try {
            secureStore.upsert(
                key = key,
                value = value
            )
        } catch (e: SecureStorageError) {
            Log.e(this::class.simpleName, e.message, e)
        }
    }
}
