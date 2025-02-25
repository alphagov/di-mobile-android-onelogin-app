package uk.gov.onelogin.core.tokens.domain.retrieve

import android.util.Log
import javax.inject.Inject
import javax.inject.Named
import uk.gov.android.securestore.RetrievalEvent
import uk.gov.android.securestore.SecureStore

class GetFromOpenSecureStoreImpl @Inject constructor(
    @Named("Open")
    private val secureStore: SecureStore
) : GetFromOpenSecureStore {
    override suspend fun invoke(vararg key: String): Map<String, String>? {
        return when (val retrievalEvent = secureStore.retrieve(*key)) {
            is RetrievalEvent.Failed -> {
                Log.e(this::class.simpleName, "Reason: ${retrievalEvent.reason}")
                null
            }
            is RetrievalEvent.Success -> retrievalEvent.value
        }
    }
}
