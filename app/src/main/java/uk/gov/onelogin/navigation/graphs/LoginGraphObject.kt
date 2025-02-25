package uk.gov.onelogin.navigation.graphs

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.data.MainNavRoutes
import uk.gov.onelogin.core.navigation.domain.hasPreviousBackStack
import uk.gov.onelogin.core.ui.pages.loading.LoadingScreen
import uk.gov.onelogin.features.error.ui.signin.SignInErrorScreen
import uk.gov.onelogin.features.login.ui.signin.PasscodeInfoScreen
import uk.gov.onelogin.features.login.ui.signin.biooptin.BiometricsOptInScreen
import uk.gov.onelogin.features.login.ui.signin.splash.SplashScreen
import uk.gov.onelogin.features.login.ui.signin.welcome.WelcomeScreen
import uk.gov.onelogin.features.optin.ui.OptInScreen
import uk.gov.onelogin.navigation.graphs.ErrorGraphObject.OFFLINE_ERROR_TRY_AGAIN_KEY

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
                SignInErrorScreen(
                    goBack = {
                        navController.navigate(LoginRoutes.Welcome.getRoute()) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                ) {
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
