package uk.gov.onelogin.core.tokens.domain.retrieve

import javax.inject.Inject
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

class GetPersistentIdImpl @Inject constructor(
    val getFromOpenSecureStore: GetFromOpenSecureStore
) : GetPersistentId {
    override suspend fun invoke(): String? {
        return getFromOpenSecureStore(AuthTokenStoreKeys.PERSISTENT_ID_KEY)?.get(
            AuthTokenStoreKeys.PERSISTENT_ID_KEY
        )
    }
}
