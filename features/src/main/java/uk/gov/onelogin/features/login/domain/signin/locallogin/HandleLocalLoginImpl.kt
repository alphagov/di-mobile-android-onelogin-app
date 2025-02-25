package uk.gov.onelogin.features.login.domain.signin.locallogin

import androidx.fragment.app.FragmentActivity
import javax.inject.Inject
import uk.gov.android.authentication.login.TokenResponse
import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.tokens.data.LocalAuthStatus
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.domain.IsAccessTokenExpired
import uk.gov.onelogin.core.tokens.domain.retrieve.GetFromEncryptedSecureStore
import uk.gov.onelogin.core.tokens.domain.retrieve.GetTokenExpiry
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

class HandleLocalLoginImpl @Inject constructor(
    private val getTokenExpiry: GetTokenExpiry,
    private val tokenRepository: TokenRepository,
    private val isAccessTokenExpired: IsAccessTokenExpired,
    private val getFromEncryptedSecureStore: GetFromEncryptedSecureStore,
    private val bioPrefHandler: BiometricPreferenceHandler
) : HandleLocalLogin {
    override suspend fun invoke(
        fragmentActivity: FragmentActivity,
        callback: (LocalAuthStatus) -> Unit
    ) {
        if (!isAccessTokenExpired() && bioPrefHandler.getBioPref() != BiometricPreference.NONE) {
            getFromEncryptedSecureStore(
                fragmentActivity,
                AuthTokenStoreKeys.ACCESS_TOKEN_KEY,
                AuthTokenStoreKeys.ID_TOKEN_KEY
            ) {
                if (it is LocalAuthStatus.Success) {
                    // These should never be returned null - secure store checks for all values to not be null
                    val accessToken = it.payload[AuthTokenStoreKeys.ACCESS_TOKEN_KEY]
                    val idToken = it.payload[AuthTokenStoreKeys.ID_TOKEN_KEY]
                    if (!accessToken.isNullOrEmpty() && !idToken.isNullOrEmpty()) {
                        tokenRepository.setTokenResponse(
                            TokenResponse(
                                accessToken = accessToken,
                                idToken = idToken,
                                tokenType = "",
                                accessTokenExpirationTime = getTokenExpiry() ?: 0
                            )
                        )
                    } else {
                        callback(LocalAuthStatus.ManualSignIn)
                    }
                }
                callback(it)
            }
        } else {
            if (getTokenExpiry() == null) {
                callback(LocalAuthStatus.ManualSignIn)
            } else {
                callback(LocalAuthStatus.ReAuthSignIn)
            }
        }
    }
}
