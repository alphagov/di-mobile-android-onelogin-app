package uk.gov.onelogin.ui.error

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.gov.onelogin.ui.error.unavailable.AppUnavailableScreen
import uk.gov.onelogin.ui.error.update.UpdateRequiredScreen

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
                val context = LocalActivity.current as FragmentActivity
                SignOutErrorScreen {
                    context.finishAndRemoveTask()
                }
            }
            composable(
                route = ErrorRoutes.Offline.getRoute()
            ) {
                OfflineErrorScreen {
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
                val context = LocalActivity.current as Activity
                BackHandler(enabled = true) {
                    // Close/ terminate the app
                    context.finishAndRemoveTask()
                }
                UpdateRequiredScreen()
            }
            composable(
                route = ErrorRoutes.Unavailable.getRoute()
            ) {
                AppUnavailableScreen()
            }
        }
    }
}
