package uk.gov.onelogin.core.tokens.domain.save

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys.TOKEN_EXPIRY_KEY
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys.TOKEN_SHARED_PREFS

class SaveTokenExpiryImpl @Inject constructor(
    @ApplicationContext
    context: Context
) : SaveTokenExpiry {
    private val sharedPrefs = context.getSharedPreferences(
        TOKEN_SHARED_PREFS,
        Context.MODE_PRIVATE
    )

    override fun invoke(expiry: Long) {
        with(sharedPrefs.edit()) {
            putLong(TOKEN_EXPIRY_KEY, expiry)
            apply()
        }
    }
}
