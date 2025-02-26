package uk.gov.onelogin.core.navigation.data

import uk.gov.onelogin.core.navigation.domain.NavRoute

sealed class LoginRoutes(private val route: String) : NavRoute {
    data object Root : LoginRoutes("/login")

    data object Start : LoginRoutes("/login/start")

    data object Welcome : LoginRoutes("/login/welcome")

    data object Loading : LoginRoutes("/login/loading")

    data object BioOptIn : LoginRoutes("/login/bio_opt_in")

    data object SignInError : LoginRoutes("/login/sign_in_error")

    data object AnalyticsOptIn : LoginRoutes("/login/analytics_opt_in")

    override fun getRoute() = route
}
