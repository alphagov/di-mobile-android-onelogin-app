package uk.gov.onelogin.features.login.domain.signin.locallogin

import androidx.fragment.app.FragmentActivity
import uk.gov.onelogin.core.tokens.data.LocalAuthStatus

fun interface HandleLocalLogin {
    suspend operator fun invoke(
        fragmentActivity: FragmentActivity,
        callback: (LocalAuthStatus) -> Unit
    )
}
