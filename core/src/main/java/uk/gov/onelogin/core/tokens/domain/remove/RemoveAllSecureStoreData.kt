package uk.gov.onelogin.core.tokens.domain.remove

import android.util.Log
import javax.inject.Inject
import javax.inject.Named
import uk.gov.android.securestore.SecureStore
import uk.gov.android.securestore.error.SecureStorageError
import uk.gov.onelogin.core.cleaner.domain.Cleaner
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

interface RemoveAllSecureStoreData : Cleaner

class RemoveAllSecureStoreDataImpl @Inject constructor(
    @Named("Token")
    private val tokenSecureStore: SecureStore
) : RemoveAllSecureStoreData {
    override suspend fun clean(): Result<Unit> {
        return try {
            tokenSecureStore.delete(
                key = AuthTokenStoreKeys.ACCESS_TOKEN_KEY
            )
            tokenSecureStore.delete(
                key = AuthTokenStoreKeys.ID_TOKEN_KEY
            )
            Result.success(Unit)
        } catch (e: SecureStorageError) {
            Log.e(this::class.simpleName, e.message, e)
            Result.failure(e)
        }
    }
}
