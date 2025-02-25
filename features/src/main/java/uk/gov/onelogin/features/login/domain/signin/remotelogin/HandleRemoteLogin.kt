package uk.gov.onelogin.features.login.domain.signin.remotelogin

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

fun interface HandleRemoteLogin {
    suspend fun login(
        launcher: ActivityResultLauncher<Intent>,
        onFailure: () -> Unit
    )
}
