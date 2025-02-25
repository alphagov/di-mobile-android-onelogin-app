package uk.gov.onelogin.core.utils

import uk.gov.android.authentication.login.LoginSessionConfiguration

interface LocaleUtils {
    fun getLocaleAsSessionConfig(): LoginSessionConfiguration.Locale

    fun getLocale(): String
}
