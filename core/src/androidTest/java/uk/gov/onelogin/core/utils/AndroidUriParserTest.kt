package uk.gov.onelogin.core.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class AndroidUriParserTest {
    private val parser = AndroidUriParser()

    @Test
    fun checkParser() {
        val result = parser.parse("www.google.com")

        assertEquals("www.google.com", result.path)
    }
}
