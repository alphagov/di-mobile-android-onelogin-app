package uk.gov.onelogin.features

import android.content.Context
import android.content.res.Resources
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Rule

abstract class TestCase {
    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    lateinit var navController: TestNavHostController

    protected val context: Context = ApplicationProvider.getApplicationContext()

    protected val resources: Resources = context.resources

    protected val device: UiDevice =
        UiDevice.getInstance(
            InstrumentationRegistry.getInstrumentation()
        )
}
