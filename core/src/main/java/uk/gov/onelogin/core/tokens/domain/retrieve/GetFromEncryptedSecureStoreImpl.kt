package uk.gov.onelogin.core.tokens.domain.retrieve

import androidx.fragment.app.FragmentActivity
import javax.inject.Inject
import javax.inject.Named
import uk.gov.android.onelogin.core.R
import uk.gov.android.securestore.RetrievalEvent
import uk.gov.android.securestore.SecureStore
import uk.gov.android.securestore.authentication.AuthenticatorPromptConfiguration
import uk.gov.android.securestore.error.SecureStoreErrorType
import uk.gov.onelogin.core.tokens.data.LocalAuthStatus

class GetFromEncryptedSecureStoreImpl @Inject constructor(
    @Named("Token")
    private val secureStore: SecureStore
) : GetFromEncryptedSecureStore {
    override suspend fun invoke(
        context: FragmentActivity,
        vararg key: String,
        callback: (LocalAuthStatus) -> Unit
    ) {
        val authPromptConfig = AuthenticatorPromptConfiguration(
            title = context.getString(R.string.app_authenticationDialogueTitle)
        )

        val result = secureStore.retrieveWithAuthentication(
            key = key,
            authPromptConfig = authPromptConfig,
            context = context
        )

        when (result) {
            is RetrievalEvent.Success -> callback(LocalAuthStatus.Success(result.value))

            is RetrievalEvent.Failed -> {
                val localAuthStatus = when (result.type) {
                    SecureStoreErrorType.GENERAL -> LocalAuthStatus.SecureStoreError

                    SecureStoreErrorType.USER_CANCELED_BIO_PROMPT ->
                        LocalAuthStatus.UserCancelled

                    SecureStoreErrorType.FAILED_BIO_PROMPT -> LocalAuthStatus.BioCheckFailed
                    SecureStoreErrorType.NOT_FOUND -> LocalAuthStatus.SecureStoreError
                }
                callback(localAuthStatus)
            }
        }
    }
}
