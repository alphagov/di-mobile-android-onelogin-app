package uk.gov.onelogin.core.navigation.data

import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorRoutesTest {
    @Test
    fun verifyRoutes() {
        listOf(
            ErrorRoutes.Root.getRoute() to "/error",
            ErrorRoutes.Generic.getRoute() to "/error/generic",
            ErrorRoutes.Offline.getRoute() to "/error/offline",
            ErrorRoutes.SignOut.getRoute() to "/error/sign_out",
            ErrorRoutes.UpdateRequired.getRoute() to "/error/update_required",
            ErrorRoutes.Unavailable.getRoute() to "/error/unavailable"
        ).forEach { assertEquals(it.second, it.first) }
    }
}
