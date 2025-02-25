package uk.gov.onelogin

import androidx.test.core.app.launchActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.e2e.controller.TestCase
import uk.gov.onelogin.navigation.NavigatorModule

@HiltAndroidTest
@UninstallModules(NavigatorModule::class)
class MainActivityTest : TestCase() {

    @BindValue
    val navigator: Navigator = mock()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testNavigationSetup() {
        launchActivity<MainActivity>()
        verify(navigator).setController(any())
    }
}
