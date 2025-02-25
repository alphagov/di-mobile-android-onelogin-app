package uk.gov.onelogin.login.appintegrity

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.appCheck
import com.google.firebase.initialize
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import uk.gov.android.authentication.integrity.appcheck.model.AppCheckToken
import uk.gov.android.authentication.integrity.appcheck.usecase.AppChecker

class FirebaseAppCheck @Inject constructor(
    appCheckFactory: AppCheckProviderFactory,
    context: Context
) : AppChecker {
    private val appCheck = Firebase.appCheck

    init {
        Firebase.appCheck.installAppCheckProviderFactory(
            appCheckFactory
        )
        Firebase.initialize(context)
    }

    override suspend fun getAppCheckToken(): Result<AppCheckToken> {
        return try {
            Result.success(
                AppCheckToken(appCheck.limitedUseAppCheckToken.await().token)
            )
        } catch (e: FirebaseException) {
            Result.failure(e)
        }
    }
}
