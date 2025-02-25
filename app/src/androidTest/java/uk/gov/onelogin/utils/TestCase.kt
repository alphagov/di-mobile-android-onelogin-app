package uk.gov.onelogin.utils

import android.content.Context
import android.content.res.Resources
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Rule
import uk.gov.onelogin.HiltTestActivity

abstract class TestCase {
    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    lateinit var navController: TestNavHostController

    protected val context: Context = ApplicationProvider.getApplicationContext()

    protected val resources: Resources = context.resources

    protected val device: UiDevice =
        UiDevice.getInstance(
            InstrumentationRegistry.getInstrumentation()
        )
}
