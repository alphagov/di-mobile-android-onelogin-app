package uk.gov.onelogin.core.biometrics.domain

import android.content.Context
import androidx.annotation.VisibleForTesting
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.cleaner.domain.Cleaner

interface BiometricPreferenceHandler : Cleaner {
    fun setBioPref(pref: BiometricPreference)

    fun getBioPref(): BiometricPreference?
}

class BiometricPreferenceHandlerImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : BiometricPreferenceHandler {
    private val sharedPrefs = context.getSharedPreferences(
        SHARED_PREFS_ID,
        Context.MODE_PRIVATE
    )

    override fun setBioPref(pref: BiometricPreference) {
        with(sharedPrefs.edit()) {
            putString(BIO_PREF, pref.name)
            apply()
        }
    }

    override fun getBioPref(): BiometricPreference? {
        return when (sharedPrefs.getString(BIO_PREF, null)) {
            BiometricPreference.BIOMETRICS.name -> BiometricPreference.BIOMETRICS
            BiometricPreference.PASSCODE.name -> BiometricPreference.PASSCODE
            BiometricPreference.NONE.name -> BiometricPreference.NONE
            else -> null
        }
    }

    override suspend fun clean(): Result<Unit> {
        with(sharedPrefs.edit()) {
            clear()
            commit()
        }
        return Result.success(Unit)
    }

    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        const val SHARED_PREFS_ID = "bio_shared_prefs"

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        const val BIO_PREF = "bio_pref"
    }
}
