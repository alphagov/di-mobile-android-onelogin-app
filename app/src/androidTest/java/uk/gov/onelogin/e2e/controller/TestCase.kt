package uk.gov.onelogin.e2e.controller

import android.content.Context
import android.content.res.Resources
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import uk.gov.onelogin.utils.FlakyTestRule

open class TestCase {
    @get:Rule(order = 1)
    val hiltRule by lazy { HiltAndroidRule(this) }

    @get:Rule(order = 2)
    var testNameRule: TestCaseNameWatcher = TestCaseNameWatcher()

    @get:Rule(order = 4)
    var flakyTestRule = FlakyTestRule()

    private var screenshotName: String? = null

    protected open val phoneController =
        PhoneController(testNameRule = testNameRule)

    protected val context: Context = ApplicationProvider.getApplicationContext()

    protected val resources: Resources = context.resources

    @Before
    fun setScreenshotName() {
        screenshotName = "${testNameRule.methodName}_${resources.configuration.locales.get(0)}"
    }
}
