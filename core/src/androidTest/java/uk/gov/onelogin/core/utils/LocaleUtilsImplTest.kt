package uk.gov.onelogin.core.utils

import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Test
import uk.gov.android.authentication.login.LoginSessionConfiguration
import uk.gov.onelogin.core.TestCase

class LocaleUtilsImplTest : TestCase() {
    private val useCase = LocaleUtilsImpl(context)

    @Test
    fun getLocaleAsSessionConfigEn() {
        val localeList = LocaleListCompat.create(Locale("en"), Locale("cy"))
        ConfigurationCompat.setLocales(context.resources.configuration, localeList)
        val result = useCase.getLocaleAsSessionConfig()
        assertEquals(LoginSessionConfiguration.Locale.EN, result)
    }

    @Test
    fun getLocaleAsSessionConfigCy() {
        val localeList = LocaleListCompat.create(Locale("cy"), Locale("en"))
        ConfigurationCompat.setLocales(context.resources.configuration, localeList)
        val result = useCase.getLocaleAsSessionConfig()
        assertEquals(LoginSessionConfiguration.Locale.CY, result)
    }
}
