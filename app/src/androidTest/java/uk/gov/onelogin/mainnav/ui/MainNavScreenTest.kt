package uk.gov.onelogin.mainnav.ui

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.gov.android.features.FeatureFlags
import uk.gov.android.onelogin.R
import uk.gov.android.wallet.core.R as walletR
import uk.gov.onelogin.TestCase
import uk.gov.onelogin.features.FeaturesModule
import uk.gov.onelogin.mainnav.nav.BottomNavDestination

@HiltAndroidTest
@UninstallModules(FeaturesModule::class)
class MainNavScreenTest : TestCase() {
    private val homeTab = hasText(resources.getString(R.string.app_home))
    private val walletTab = hasText(resources.getString(R.string.app_wallet))
    private val settingsTab = hasText(resources.getString(R.string.app_settingsTitle))

    @BindValue
    val featureFlags: FeatureFlags = mock()

    @Test
    fun checkBottomOptionsDisplayed() {
        whenever(featureFlags[any()]).thenReturn(true)
        setup()
        composeTestRule.onAllNodes(homeTab)[1].isDisplayed() // we have double match of `Home` text
        composeTestRule.waitUntil(5000L) { composeTestRule.onNode(walletTab).isDisplayed() }
        composeTestRule.onNode(walletTab).isDisplayed()
        composeTestRule.onNode(settingsTab).isDisplayed()

        assertEquals(
            BottomNavDestination.Home.key,
            navController.currentDestination?.route
        )
    }

    @Test
    fun goesToWalletOnClick() {
        whenever(featureFlags[any()]).thenReturn(true)
        setup()
        composeTestRule.waitUntil(5000L) { composeTestRule.onNode(walletTab).isDisplayed() }
        composeTestRule.onNode(walletTab).performClick()

        assertEquals(
            BottomNavDestination.Wallet.key,
            navController.currentDestination?.route
        )

        composeTestRule.onNodeWithText(
            resources.getString(walletR.string.introCardTitle)
        ).assertIsDisplayed()
    }

    @Test
    fun checkWalletNotDisplayed() {
        whenever(featureFlags[any()]).thenReturn(false)
        setup()
        composeTestRule.onAllNodes(homeTab)[1].isDisplayed() // we have double match of `Home` text
        composeTestRule.onNode(walletTab).isNotDisplayed()
        composeTestRule.onNode(settingsTab).isDisplayed()

        assertEquals(
            BottomNavDestination.Home.key,
            navController.currentDestination?.route
        )
    }

    @Test
    fun goesToSettingsOnClick() {
        setup()
        composeTestRule.onNode(settingsTab).performClick()

        assertEquals(
            BottomNavDestination.Settings.key,
            navController.currentDestination?.route
        )

        composeTestRule.onAllNodesWithText(
            resources.getString(R.string.app_settingsTitle)
        ).assertCountEquals(2)
    }

    private fun setup() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MainNavScreen(navController)
        }
    }
}
