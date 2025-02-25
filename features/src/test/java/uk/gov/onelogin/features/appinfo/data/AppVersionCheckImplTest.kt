package uk.gov.onelogin.features.appinfo.data

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test
import uk.gov.onelogin.features.TestUtils
import uk.gov.onelogin.features.appinfo.AppInfoUtils
import uk.gov.onelogin.features.appinfo.AppInfoUtilsImpl
import uk.gov.onelogin.features.appinfo.data.model.AppInfoServiceState

class AppVersionCheckImplTest {
    private val appInfoUtils: AppInfoUtils = AppInfoUtilsImpl()
    private val sut = AppVersionCheckImpl(appInfoUtils, "1.0.0")

    @Test
    fun versionCheckSuccessful() {
        var result = sut.compareVersions(TestUtils.appInfoData)
        assertEquals(AppInfoServiceState.Successful(TestUtils.appInfoData), result)
        result = sut.compareVersions(TestUtils.additionalAppInfoData)
        assertEquals(AppInfoServiceState.Successful(TestUtils.additionalAppInfoData), result)
    }

    @Test
    fun versionCheckUpdateRequired() {
        val result = sut.compareVersions(TestUtils.updateRequiredAppInfoData)
        assertEquals(AppInfoServiceState.UpdateRequired, result)
    }

    @Test
    fun versionCheckError() {
        val result = sut.compareVersions(TestUtils.extractVersionErrorAppInfoData)
        assertEquals(AppInfoServiceState.Unavailable, result)
    }
}
