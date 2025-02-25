package uk.gov.onelogin.core.tokens.domain.remove

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import uk.gov.onelogin.core.cleaner.domain.Cleaner
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys.TOKEN_EXPIRY_KEY
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys.TOKEN_SHARED_PREFS

interface RemoveTokenExpiry : Cleaner

class RemoveTokenExpiryImpl @Inject constructor(
    @ApplicationContext
    context: Context
) : RemoveTokenExpiry {
    private val sharedPrefs =
        context.getSharedPreferences(TOKEN_SHARED_PREFS, Context.MODE_PRIVATE)

    override suspend fun clean(): Result<Unit> {
        with(sharedPrefs.edit()) {
            remove(TOKEN_EXPIRY_KEY)
            commit()
        }
        return Result.success(Unit)
    }
}
