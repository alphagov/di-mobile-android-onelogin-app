package uk.gov.onelogin.ui.error

import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.testing.TestNavHostController
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.wheneverBlocking
import uk.gov.android.onelogin.R
import uk.gov.onelogin.TestCase
import uk.gov.onelogin.TestUtils
import uk.gov.onelogin.appinfo.AppInfoApiModule
import uk.gov.onelogin.appinfo.service.domain.AppInfoService
import uk.gov.onelogin.appinfo.service.domain.model.AppInfoServiceState
import uk.gov.onelogin.appinfo.source.domain.source.AppInfoLocalSource
import uk.gov.onelogin.ui.error.ErrorGraphObject.errorGraph

@HiltAndroidTest
@UninstallModules(AppInfoApiModule::class)
class ErrorGraphObjectTest : TestCase() {
    @BindValue
    val mockAppInfoService: AppInfoService = mock()

    @BindValue
    val appInfoLocalSource: AppInfoLocalSource = mock()

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavHost(
                navController = navController,
                startDestination = ErrorRoutes.Root.getRoute()
            ) {
                errorGraph(navController)
            }
        }
        wheneverBlocking { mockAppInfoService.get() }.thenAnswer {
            AppInfoServiceState.Successful(TestUtils.appInfoData)
        }
    }

    @Test
    fun navigateToSignOutError() {
        composeTestRule.runOnUiThread {
            navController.setCurrentDestination(ErrorRoutes.SignOut.getRoute())
        }

        composeTestRule.onNodeWithText(
            resources.getString(R.string.app_signOutErrorTitle)
        ).assertExists()
    }

    @Test
    fun navigateToGenericError() {
        composeTestRule.runOnUiThread {
            navController.setCurrentDestination(ErrorRoutes.Generic.getRoute())
        }
        composeTestRule.onNodeWithText(
            resources.getString(R.string.app_somethingWentWrongErrorTitle)
        ).assertExists()
    }

    @Test
    fun navigateToOfflineError() {
        composeTestRule.runOnUiThread {
            navController.setCurrentDestination(ErrorRoutes.Offline.getRoute())
        }
        composeTestRule.onNodeWithText(
            resources.getString(R.string.app_networkErrorTitle)
        ).assertExists()
    }

    @Test
    fun navigateToUpdateRequiredError() {
        composeTestRule.runOnUiThread {
            navController.setCurrentDestination(ErrorRoutes.UpdateRequired.getRoute())
        }
        composeTestRule.onNodeWithText(
            resources.getString(R.string.app_updateApp_Title)
        ).assertExists()
        composeTestRule.runOnUiThread {
            navController.popBackStack()
        }
        composeTestRule.onNodeWithText(
            resources.getString(R.string.app_updateApp_Title)
        ).assertDoesNotExist()
    }

    @Test
    fun navigateToAppUnavailable() {
        composeTestRule.runOnUiThread {
            navController.setCurrentDestination(ErrorRoutes.Unavailable.getRoute())
        }
        composeTestRule.onNodeWithText(
            resources.getString(R.string.app_appUnavailableTitle)
        ).assertExists()
    }
}
