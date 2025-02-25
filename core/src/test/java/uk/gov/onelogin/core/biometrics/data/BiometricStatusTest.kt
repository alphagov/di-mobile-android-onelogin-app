package uk.gov.onelogin.core.biometrics.data

import androidx.biometric.BiometricManager
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class BiometricStatusTest {
    @Test
    fun success() {
        val status = BiometricStatus.SUCCESS
        assertEquals(expected = status, actual = BiometricStatus.valueOf("SUCCESS"))
    }

    @Test
    fun `no hardware`() {
        val status = BiometricStatus.NO_HARDWARE
        assertEquals(expected = status, actual = BiometricStatus.valueOf("NO_HARDWARE"))
    }

    @Test
    fun `hardware unavailable`() {
        val status = BiometricStatus.HARDWARE_UNAVAILABLE
        assertEquals(expected = status, actual = BiometricStatus.valueOf("HARDWARE_UNAVAILABLE"))
    }

    @Test
    fun `not enrolled`() {
        val status = BiometricStatus.NOT_ENROLLED
        assertEquals(expected = status, actual = BiometricStatus.valueOf("NOT_ENROLLED"))
    }

    @Test
    fun unknown() {
        val status = BiometricStatus.UNKNOWN
        assertEquals(expected = status, actual = BiometricStatus.valueOf("UNKNOWN"))
    }

    @Test
    fun values() {
        val list = BiometricStatus.values()
        assertEquals(expected = 5, actual = list.size)
    }

    @Test
    fun entries() {
        val list = BiometricStatus.entries
        assertEquals(expected = 5, actual = list.size)
    }

    @ParameterizedTest
    @MethodSource("biometricStatusArgs")
    fun `map BiometricManager status code to BiometricStatus`(
        statusCode: Int,
        expected: BiometricStatus
    ) {
        // Given an Android BiometricManager status code
        // When calling `forAndroidInt`
        val actual = BiometricStatus.forAndroidInt(statusCode)
        // Then the actual BiometricStatus matches the expected
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun biometricStatusArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    BiometricManager.BIOMETRIC_SUCCESS,
                    BiometricStatus.SUCCESS
                ),
                Arguments.of(
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                    BiometricStatus.NO_HARDWARE
                ),
                Arguments.of(
                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
                    BiometricStatus.HARDWARE_UNAVAILABLE
                ),
                Arguments.of(
                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
                    BiometricStatus.NOT_ENROLLED
                ),
                Arguments.of(
                    BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED,
                    BiometricStatus.UNKNOWN
                )
            )
    }
}
