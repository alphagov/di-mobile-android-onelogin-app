package uk.gov.onelogin.features.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController

fun ComposeContentTestRule.setupComposeTestRule(
    screenToLoad: @Composable (NavHostController) -> Unit
): TestNavHostController {
    lateinit var navHostController: TestNavHostController

    setContent {
        navHostController = TestNavHostController(LocalContext.current)
        navHostController.navigatorProvider.addNavigator(ComposeNavigator())
        screenToLoad(navHostController)
    }

    return navHostController
}
