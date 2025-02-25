package uk.gov.onelogin.core.tokens.domain.retrieve

import javax.inject.Inject
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.domain.extractEmailFromIdToken

class GetEmailImpl @Inject constructor(
    val tokenRepository: TokenRepository
) : GetEmail {
    override fun invoke(): String? {
        val idToken: String = tokenRepository.getTokenResponse()?.idToken ?: return null
        val email = idToken.extractEmailFromIdToken()
        return email
    }
}
