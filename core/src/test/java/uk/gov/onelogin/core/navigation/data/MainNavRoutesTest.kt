package uk.gov.onelogin.core.navigation.data

import kotlin.test.Test
import kotlin.test.assertEquals

class MainNavRoutesTest {
    @Test
    fun verifyRoutes() {
        listOf(
            MainNavRoutes.Root.getRoute() to "/home",
            MainNavRoutes.Start.getRoute() to "/home/start"
        ).forEach { assertEquals(it.second, it.first) }
    }
}
