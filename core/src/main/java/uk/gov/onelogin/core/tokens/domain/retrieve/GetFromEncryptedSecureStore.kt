package uk.gov.onelogin.core.tokens.domain.retrieve

import androidx.fragment.app.FragmentActivity
import uk.gov.onelogin.core.tokens.data.LocalAuthStatus

fun interface GetFromEncryptedSecureStore {
    /**
     * Use case for getting data from a secure store instance.
     *
     * @param context Must be a FragmentActivity context (due to authentication prompt)
     * @param key [String] value of key value pairs to retrieve
     * @param callback which is used for handling [LocalAuthStatus] result
     */
    suspend operator fun invoke(
        context: FragmentActivity,
        vararg key: String,
        callback: (LocalAuthStatus) -> Unit
    )
}
