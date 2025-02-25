package uk.gov.onelogin.core.biometrics.domain

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import uk.gov.onelogin.core.biometrics.data.BiometricStatus

class BiometricManagerTest {
    private lateinit var manager: BiometricManager

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        manager = BiometricManagerImpl(context)
    }

    @Ignore("Needs to find a way to get it working on all devices")
    @Test
    fun successReturnedFromBiometricManager() {
        val actual = manager.canAuthenticate()
        assertEquals(BiometricStatus.SUCCESS, actual)
    }
}
