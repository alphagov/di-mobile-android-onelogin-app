package uk.gov.onelogin.core.navigation.data

import uk.gov.onelogin.core.navigation.domain.NavRoute

sealed class SignOutRoutes(private val route: String) : NavRoute {
    data object Root : SignOutRoutes("/sign_out")

    data object Start : SignOutRoutes("/sign_out/start")

    data object Info : SignOutRoutes("/sign_out/info")

    data object ReAuthError : SignOutRoutes("/sign_out/re_auth_error")

    override fun getRoute() = route
}
