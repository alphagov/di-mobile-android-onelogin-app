package uk.gov.onelogin.features.appinfo

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AppInfoUtilsTest {
    private val correctV = "1.0.0"
    private val correctVersion = "v1.0.0"
    private val githubVersion = "1729695540-1.0.0"
    private val incorrectVersion = ""
    private val sut = AppInfoUtilsImpl()

    @Test
    fun `check conventional correct version format`() {
        var result = sut.getComparableAppVersion(correctVersion)
        assertEquals(listOf(1, 0, 0), result)
        result = sut.getComparableAppVersion(githubVersion)
        assertEquals(listOf(1, 0, 0), result)
        result = sut.getComparableAppVersion(correctV)
        assertEquals(listOf(1, 0, 0), result)
    }

    @Test
    fun `conventional commits violation - empty version`() {
        assertThrows<AppInfoUtils.AppError.IllegalVersionFormat> {
            sut.getComparableAppVersion(incorrectVersion)
        }
    }
}
