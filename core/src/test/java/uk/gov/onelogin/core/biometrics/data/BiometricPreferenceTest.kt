package uk.gov.onelogin.core.biometrics.data

import kotlin.test.Test
import kotlin.test.assertEquals

class BiometricPreferenceTest {
    @Test
    fun biometrics() {
        val preference = BiometricPreference.BIOMETRICS
        assertEquals(expected = preference, actual = BiometricPreference.valueOf("BIOMETRICS"))
    }

    @Test
    fun none() {
        val preference = BiometricPreference.NONE
        assertEquals(expected = preference, actual = BiometricPreference.valueOf("NONE"))
    }

    @Test
    fun passcode() {
        val preference = BiometricPreference.PASSCODE
        assertEquals(expected = preference, actual = BiometricPreference.valueOf("PASSCODE"))
    }

    @Test
    fun values() {
        val list = BiometricPreference.values()
        assertEquals(expected = 3, actual = list.size)
    }

    @Test
    fun entries() {
        val list = BiometricPreference.entries
        assertEquals(expected = 3, actual = list.size)
    }
}
