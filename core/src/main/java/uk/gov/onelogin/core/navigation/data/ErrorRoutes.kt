package uk.gov.onelogin.core.navigation.data

import uk.gov.onelogin.core.navigation.domain.NavRoute

sealed class ErrorRoutes(private val route: String) : NavRoute {
    data object Root : ErrorRoutes("/error")

    data object Generic : ErrorRoutes("/error/generic")

    data object Offline : ErrorRoutes("/error/offline")

    data object SignOut : ErrorRoutes("/error/sign_out")

    data object UpdateRequired : ErrorRoutes("/error/update_required")

    data object Unavailable : ErrorRoutes("/error/unavailable")

    override fun getRoute() = route
}
