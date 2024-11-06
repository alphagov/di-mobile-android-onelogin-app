package uk.gov.onelogin.appcheck

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import uk.gov.android.authentication.integrity.appcheck.AppChecker

class FirebaseAppCheckTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val factory = DebugAppCheckProviderFactory.getInstance()
    private lateinit var sut: AppChecker

    @Before
    fun setup() {
        sut = FirebaseAppCheck(factory)
        sut.init(context)
    }

    @Test
    fun test_initialise() {
        assertNotNull(Firebase.appCheck)
    }
}
