package uk.gov.onelogin.core.tokens.domain.save

import javax.inject.Inject
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.domain.extractPersistentIdFromIdToken
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

class SaveTokensImpl @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val saveToEncryptedSecureStore: SaveToEncryptedSecureStore,
    private val saveToOpenSecureStore: SaveToOpenSecureStore
) : SaveTokens {
    override suspend fun invoke() {
        val tokens = tokenRepository.getTokenResponse()
        tokens?.let { tokenResponse ->
            saveToEncryptedSecureStore(
                key = AuthTokenStoreKeys.ACCESS_TOKEN_KEY,
                value = tokenResponse.accessToken
            )
            tokenResponse.idToken.extractPersistentIdFromIdToken()
                ?.let { id ->
                    saveToOpenSecureStore.save(
                        key = AuthTokenStoreKeys.PERSISTENT_ID_KEY,
                        value = id
                    )
                }
            saveToEncryptedSecureStore(
                key = AuthTokenStoreKeys.ID_TOKEN_KEY,
                value = tokenResponse.idToken
            )
        }
    }
}
