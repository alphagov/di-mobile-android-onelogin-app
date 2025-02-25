package uk.gov.onelogin.features

import android.content.Context
import android.content.res.Resources
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import uk.gov.onelogin.core.TestActivity

abstract class FragmentActivityTestCase {
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    lateinit var navController: TestNavHostController

    protected val context: Context = ApplicationProvider.getApplicationContext()

    protected val resources: Resources = context.resources
}
