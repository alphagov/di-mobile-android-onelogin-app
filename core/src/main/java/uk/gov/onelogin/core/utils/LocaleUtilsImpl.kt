package uk.gov.onelogin.core.utils

import android.content.Context
import androidx.core.os.ConfigurationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import uk.gov.android.authentication.login.LoginSessionConfiguration

class LocaleUtilsImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : LocaleUtils {
    override fun getLocaleAsSessionConfig(): LoginSessionConfiguration.Locale = when (getLocale()) {
        "cy" -> LoginSessionConfiguration.Locale.CY
        else -> LoginSessionConfiguration.Locale.EN
    }

    override fun getLocale(): String {
        val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
        val locale = when (currentLocale?.language) {
            "cy" -> "cy"
            else -> "en"
        }
        return locale
    }
}
