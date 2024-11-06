package uk.gov.onelogin.appcheck

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.appCheck
import com.google.firebase.initialize
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import uk.gov.android.authentication.integrity.appcheck.AppChecker
import uk.gov.android.authentication.integrity.model.AppCheckToken

class FirebaseAppCheck @Inject constructor(
    private val appCheckFactory: AppCheckProviderFactory,
    private val context: Context
) : AppChecker {
    private val appCheck = Firebase.appCheck

    init {
        Firebase.appCheck.installAppCheckProviderFactory(
            appCheckFactory
        )
        Firebase.initialize(context)
    }

    override suspend fun getAppCheckToken(): Result<AppCheckToken> {
        Log.d("FirebaseAppIntegrityCall", "${appCheck.getAppCheckToken(false).await()}")
        return try {
            Result.success(
                AppCheckToken(appCheck.getAppCheckToken(false).await().token)
            )
        } catch (e: FirebaseException) {
            Result.failure(e)
        }
    }
}
