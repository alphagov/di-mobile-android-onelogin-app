package uk.gov.onelogin.core.biometrics.domain

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import java.util.stream.Stream
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandlerImpl.Companion.BIO_PREF
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandlerImpl.Companion.SHARED_PREFS_ID

class BiometricPreferenceHandlerTest {
    private val mockContext: FragmentActivity = mock()
    private val mockSharedPreferences: SharedPreferences = mock()
    private val mockEditor: SharedPreferences.Editor = mock()

    private lateinit var bioPrefHandler: BiometricPreferenceHandlerImpl

    @BeforeEach
    fun setUp() {
        whenever(mockContext.getSharedPreferences(eq(SHARED_PREFS_ID), eq(Context.MODE_PRIVATE)))
            .thenReturn(mockSharedPreferences)
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)
        bioPrefHandler = BiometricPreferenceHandlerImpl(mockContext)
    }

    @Test
    fun `check setting bio pref`() {
        bioPrefHandler.setBioPref(BiometricPreference.BIOMETRICS)

        verify(mockEditor).putString(BIO_PREF, BiometricPreference.BIOMETRICS.name)
        verify(mockEditor).apply()
    }

    @ParameterizedTest
    @MethodSource("getBioPrefArgs")
    fun `check getting bio pref`(sharedPrefReturn: BiometricPreference?) {
        whenever(mockSharedPreferences.getString(eq(BIO_PREF), eq(null))).thenReturn(
            sharedPrefReturn?.name
        )
        val bioPref = bioPrefHandler.getBioPref()

        assertEquals(sharedPrefReturn, bioPref)
    }

    @Test
    fun `check clear preference`() =
        runTest {
            val result = bioPrefHandler.clean()

            verify(mockEditor).clear()
            verify(mockEditor).commit()
            assertEquals(Result.success(Unit), result)
        }

    companion object {
        @JvmStatic
        fun getBioPrefArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of(BiometricPreference.BIOMETRICS),
                Arguments.of(BiometricPreference.PASSCODE),
                Arguments.of(BiometricPreference.NONE),
                Arguments.of(null)
            )
    }
}
