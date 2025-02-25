package uk.gov.onelogin.core.tokens.domain.save

import android.util.Log
import javax.inject.Inject
import javax.inject.Named
import uk.gov.android.securestore.SecureStore
import uk.gov.android.securestore.error.SecureStorageError

class SaveToOpenSecureStoreImpl @Inject constructor(
    @Named("Open")
    private val secureStore: SecureStore
) : SaveToOpenSecureStore {
    override suspend fun save(
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

    override suspend fun save(
        key: String,
        value: Number
    ) {
        try {
            secureStore.upsert(
                key = key,
                value = value.toString()
            )
        } catch (e: SecureStorageError) {
            Log.e(this::class.simpleName, e.message, e)
        }
    }
}
