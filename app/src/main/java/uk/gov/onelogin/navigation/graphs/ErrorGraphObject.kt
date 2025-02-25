package uk.gov.onelogin.navigation.graphs

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.gov.onelogin.core.navigation.data.ErrorRoutes
import uk.gov.onelogin.features.error.ui.SignOutErrorScreen
import uk.gov.onelogin.features.error.ui.generic.GenericErrorScreen
import uk.gov.onelogin.features.error.ui.offline.OfflineErrorScreen
import uk.gov.onelogin.features.error.ui.unavailable.AppUnavailableScreen
import uk.gov.onelogin.features.error.ui.update.ErrorUpdateRequiredScreen

object ErrorGraphObject {
    const val OFFLINE_ERROR_TRY_AGAIN_KEY: String = "OFFLINE_ERROR_TRY_AGAIN_KEY"

    fun NavGraphBuilder.errorGraph(navController: NavHostController) {
        navigation(
            startDestination = ErrorRoutes.Generic.getRoute(),
            route = ErrorRoutes.Root.getRoute()
        ) {
            composable(
                route = ErrorRoutes.Generic.getRoute()
            ) {
                GenericErrorScreen { navController.popBackStack() }
            }
            composable(
                route = ErrorRoutes.SignOut.getRoute()
            ) {
                val context = LocalContext.current as FragmentActivity
                SignOutErrorScreen {
                    context.finishAndRemoveTask()
                }
            }
            composable(
                route = ErrorRoutes.Offline.getRoute()
            ) {
                OfflineErrorScreen(
                    goBack = { navController.popBackStack() }
                ) {
                    navController.apply {
                        previousBackStackEntry?.savedStateHandle?.set(
                            OFFLINE_ERROR_TRY_AGAIN_KEY,
                            true
                        )
                        popBackStack()
                    }
                }
            }
            composable(
                route = ErrorRoutes.UpdateRequired.getRoute()
            ) {
                val context = LocalContext.current as Activity
                BackHandler(enabled = true) {
                    // Close/ terminate the app
                    context.finishAndRemoveTask()
                }
                ErrorUpdateRequiredScreen()
            }
            composable(
                route = ErrorRoutes.Unavailable.getRoute()
            ) {
                AppUnavailableScreen()
            }
        }
    }
}
