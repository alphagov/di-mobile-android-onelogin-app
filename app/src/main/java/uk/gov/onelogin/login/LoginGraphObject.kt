package uk.gov.onelogin.login

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.gov.onelogin.login.ui.PasscodeInfoScreen
import uk.gov.onelogin.login.ui.biooptin.BiometricsOptInScreen
import uk.gov.onelogin.login.ui.splash.SplashScreen
import uk.gov.onelogin.login.ui.welcome.WelcomeScreen
import uk.gov.onelogin.mainnav.MainNavRoutes
import uk.gov.onelogin.navigation.hasPreviousBackStack
import uk.gov.onelogin.optin.ui.OptInScreen
import uk.gov.onelogin.ui.error.ErrorGraphObject.OFFLINE_ERROR_TRY_AGAIN_KEY
import uk.gov.onelogin.ui.error.signin.SignInErrorScreen
import uk.gov.onelogin.ui.loading.LoadingScreen

object LoginGraphObject {
    @Suppress("LongMethod")
    fun NavGraphBuilder.loginGraph(
        navController: NavHostController
    ) {
        navigation(
            route = LoginRoutes.Root.getRoute(),
            startDestination = LoginRoutes.Start.getRoute()
        ) {
            composable(
                route = LoginRoutes.Start.getRoute()
            ) {
                val comingFromLockScreen = navController.hasPreviousBackStack()
                BackHandler(enabled = comingFromLockScreen) {
                    // do nothing if coming from Lock Screen
                }
                SplashScreen()
            }

            composable(
                route = LoginRoutes.Welcome.getRoute()
            ) {
                WelcomeScreen(
                    shouldTryAgain = {
                        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
                        val tryAgain = savedStateHandle?.get(OFFLINE_ERROR_TRY_AGAIN_KEY) ?: false
                        savedStateHandle?.remove<Boolean>(OFFLINE_ERROR_TRY_AGAIN_KEY)
                        tryAgain
                    }
                )
            }

            composable(
                route = LoginRoutes.Loading.getRoute()
            ) {
                LoadingScreen {}
            }

            composable(
                route = LoginRoutes.PasscodeInfo.getRoute()
            ) {
                PasscodeInfoScreen {
                    navController.navigate(MainNavRoutes.Start.getRoute())
                }
            }

            composable(
                route = LoginRoutes.BioOptIn.getRoute()
            ) {
                BackHandler(true) {
                    // do nothing
                }
                BiometricsOptInScreen()
            }

            composable(
                route = LoginRoutes.SignInError.getRoute()
            ) {
                BackHandler(true) {
                    navController.navigate(LoginRoutes.Welcome.getRoute()) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
                SignInErrorScreen {
                    navController.navigate(LoginRoutes.Start.getRoute()) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }

            composable(
                route = LoginRoutes.AnalyticsOptIn.getRoute()
            ) {
                BackHandler(true) {
                    // do nothing
                }
                OptInScreen()
            }
        }
    }
}
