package uk.gov.onelogin.core.utils

import java.util.stream.Stream
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.gov.android.securestore.AccessControlLevel
import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.tokens.utils.BiometricPrefsMapper.mapAccessControlLevel

class BiometricPrefsMapperTest {
    @ParameterizedTest
    @MethodSource("args")
    fun checkACLMappings(
        input: BiometricPreference,
        output: AccessControlLevel
    ) {
        val result = mapAccessControlLevel(input)

        assertEquals(output, result)
    }

    companion object {
        @JvmStatic
        fun args(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    BiometricPreference.BIOMETRICS,
                    AccessControlLevel.PASSCODE_AND_CURRENT_BIOMETRICS
                ),
                Arguments.of(BiometricPreference.PASSCODE, AccessControlLevel.PASSCODE),
                Arguments.of(BiometricPreference.NONE, AccessControlLevel.OPEN)
            )
    }
}
