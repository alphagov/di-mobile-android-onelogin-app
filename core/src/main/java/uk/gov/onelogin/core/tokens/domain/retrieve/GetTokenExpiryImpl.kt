package uk.gov.onelogin.core.tokens.domain.retrieve

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys.TOKEN_EXPIRY_KEY
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys.TOKEN_SHARED_PREFS

class GetTokenExpiryImpl @Inject constructor(
    @ApplicationContext
    context: Context
) : GetTokenExpiry {
    private val sharedPrefs = context.getSharedPreferences(
        TOKEN_SHARED_PREFS,
        Context.MODE_PRIVATE
    )

    override fun invoke(): Long? {
        val expiryTimestamp = sharedPrefs.getLong(TOKEN_EXPIRY_KEY, 0)
        return if (expiryTimestamp == 0L) {
            null
        } else {
            expiryTimestamp
        }
    }
}
