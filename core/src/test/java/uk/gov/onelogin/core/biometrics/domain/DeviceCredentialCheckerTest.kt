package uk.gov.onelogin.core.biometrics.domain

import android.app.KeyguardManager
import android.content.Context
import java.util.stream.Stream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.gov.onelogin.core.biometrics.data.BiometricStatus

class DeviceCredentialCheckerTest {
    private val mockContext: Context = mock()
    private val mockBiometricManager: BiometricManager = mock()
    private val credentialChecker: CredentialChecker =
        DeviceCredentialChecker(
            mockContext,
            mockBiometricManager
        )

    @ParameterizedTest
    @MethodSource("deviceSecureArgs")
    fun `check is device secure returns expected value`(expected: Boolean) {
        val mockKeyguardManager: KeyguardManager = mock()
        whenever(mockContext.getSystemService(any())).thenReturn(mockKeyguardManager)
        whenever(mockKeyguardManager.isDeviceSecure).thenReturn(expected)

        val result = credentialChecker.isDeviceSecure()

        assertEquals(expected, result)
    }

    @ParameterizedTest
    @MethodSource("biometricStatusArgs")
    fun `check correct biometric status is returned`(expectedBioStatus: BiometricStatus) {
        whenever(mockBiometricManager.canAuthenticate()).thenReturn(expectedBioStatus)

        val result = credentialChecker.biometricStatus()

        assertEquals(expectedBioStatus, result)
    }

    companion object {
        @JvmStatic
        fun deviceSecureArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of(true),
                Arguments.of(false)
            )

        @JvmStatic
        fun biometricStatusArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    BiometricStatus.SUCCESS
                ),
                Arguments.of(
                    BiometricStatus.NO_HARDWARE
                ),
                Arguments.of(
                    BiometricStatus.HARDWARE_UNAVAILABLE
                ),
                Arguments.of(
                    BiometricStatus.NOT_ENROLLED
                ),
                Arguments.of(
                    BiometricStatus.UNKNOWN
                )
            )
    }
}
