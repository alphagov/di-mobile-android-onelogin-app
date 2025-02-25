package uk.gov.onelogin.features.login.domain.signin.loginredirect

import android.content.Intent
import uk.gov.android.authentication.login.TokenResponse

fun interface HandleLoginRedirect {
    suspend fun handle(
        intent: Intent,
        onFailure: (Throwable?) -> Unit,
        onSuccess: (TokenResponse) -> Unit
    )
}
