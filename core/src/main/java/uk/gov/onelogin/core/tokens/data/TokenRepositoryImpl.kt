package uk.gov.onelogin.core.tokens.data

import javax.inject.Inject
import uk.gov.android.authentication.login.TokenResponse

class TokenRepositoryImpl @Inject constructor() : TokenRepository {
    private var tokenResponse: TokenResponse? = null

    override fun setTokenResponse(tokens: TokenResponse) {
        tokenResponse = tokens
    }

    override fun getTokenResponse(): TokenResponse? {
        return tokenResponse
    }

    override fun clearTokenResponse() {
        tokenResponse = null
    }
}
