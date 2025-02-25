package uk.gov.onelogin.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.gov.onelogin.core.navigation.data.MainNavRoutes
import uk.gov.onelogin.mainnav.ui.MainNavScreen

object MainNavGraph {
    fun NavGraphBuilder.mainNavRoutesFlow() {
        navigation(
            route = MainNavRoutes.Root.getRoute(),
            startDestination = MainNavRoutes.Start.getRoute()
        ) {
            composable(
                route = MainNavRoutes.Start.getRoute()
            ) {
                MainNavScreen()
            }
        }
    }
}
