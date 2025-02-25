package uk.gov.onelogin.navigation.graphs

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import javax.inject.Inject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.wheneverBlocking
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.MainActivity
import uk.gov.onelogin.appinfo.AppInfoApiModule
import uk.gov.onelogin.core.navigation.data.MainNavRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.e2e.controller.TestCase
import uk.gov.onelogin.features.appinfo.data.model.AppInfoServiceState
import uk.gov.onelogin.features.appinfo.domain.AppInfoLocalSource
import uk.gov.onelogin.features.appinfo.domain.AppInfoService
import uk.gov.onelogin.utils.TestUtils
import uk.gov.onelogin.utils.TestUtils.setActivity

@HiltAndroidTest
@UninstallModules(AppInfoApiModule::class)
class MainNavGraphObjectTest : TestCase() {
    @Inject
    lateinit var navigator: Navigator

    @BindValue
    val appInfoService: AppInfoService = mock()

    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @BindValue
    val appInfoLocalSource: AppInfoLocalSource = mock()

    @Before
    fun setup() {
        hiltRule.inject()

        wheneverBlocking { appInfoService.get() }.thenReturn(
            AppInfoServiceState.Successful(TestUtils.appInfoData)
        )
    }

    @Test
    fun mainGraph_startingDestination() {
        composeTestRule.setActivity {
            navigator.navigate(MainNavRoutes.Start)
        }

        composeTestRule.onAllNodesWithText(
            resources.getString(R.string.app_homeTitle)
        ).assertCountEquals(2)
    }
}
