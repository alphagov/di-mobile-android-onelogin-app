package uk.gov.onelogin.tokens.usecases

import android.util.Log
import javax.inject.Inject
import javax.inject.Named
import uk.gov.android.securestore.SecureStore
import uk.gov.android.securestore.error.SecureStorageError

fun interface SaveToSecureStore {
    /**
     * Use case for saving data into a secure store instance
     *
     * @param key [String] value of key value pair to save
     * @param value [String] value of value in key value pair to save
     */
    suspend operator fun invoke(
        key: String,
        value: String
    )
}

class SaveToSecureStoreImpl @Inject constructor(
    @Named("Token")
    private val secureStore: SecureStore
) : SaveToSecureStore {
    override suspend fun invoke(key: String, value: String) {
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
