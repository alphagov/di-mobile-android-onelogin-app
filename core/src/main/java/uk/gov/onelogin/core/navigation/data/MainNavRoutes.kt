package uk.gov.onelogin.core.navigation.data

import uk.gov.onelogin.core.navigation.domain.NavRoute

sealed class MainNavRoutes(private val route: String) : NavRoute {
    data object Root : MainNavRoutes("/home")

    data object Start : MainNavRoutes("/home/start")

    override fun getRoute() = route
}
