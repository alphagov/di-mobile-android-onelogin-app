package uk.gov.onelogin.ui.error

import uk.gov.onelogin.navigation.NavRoute

sealed class ErrorRoutes(private val route: String) : NavRoute {
    data object Root : ErrorRoutes("/error")
    data object Generic : ErrorRoutes("/error/generic")
    data object Offline : ErrorRoutes("/error/offline")
    data object SignOut : ErrorRoutes("/error/signOut")
    data object UpdateRequired : ErrorRoutes("/error/updateRequired")
    data object Unavailable : ErrorRoutes("/error/unavailable")

    override fun getRoute() = route
}
