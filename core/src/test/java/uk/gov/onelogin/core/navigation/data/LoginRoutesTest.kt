package uk.gov.onelogin.core.navigation.data

import kotlin.test.Test
import kotlin.test.assertEquals

class LoginRoutesTest {
    @Test
    fun verifyRoutes() {
        listOf(
            LoginRoutes.Root.getRoute() to "/login",
            LoginRoutes.Start.getRoute() to "/login/start",
            LoginRoutes.Welcome.getRoute() to "/login/welcome",
            LoginRoutes.Loading.getRoute() to "/login/loading",
            LoginRoutes.PasscodeInfo.getRoute() to "/login/passcode_info",
            LoginRoutes.BioOptIn.getRoute() to "/login/bio_opt_in",
            LoginRoutes.SignInError.getRoute() to "/login/sign_in_error",
            LoginRoutes.AnalyticsOptIn.getRoute() to "/login/analytics_opt_in"
        ).forEach { assertEquals(it.second, it.first) }
    }
}
