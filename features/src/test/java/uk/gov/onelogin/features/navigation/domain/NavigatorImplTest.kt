package uk.gov.onelogin.features.navigation.domain

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import uk.gov.onelogin.core.navigation.domain.NavRoute
import uk.gov.onelogin.core.navigation.domain.Navigator

class NavigatorImplTest {
    private val mockNavController: NavHostController = mock()
    private lateinit var navigator: Navigator

    @BeforeEach
    fun setup() {
        navigator = NavigatorImpl()
    }

    @Test
    fun `navigate calls correct route on navController, default inclusive`() {
        navigator.setController(mockNavController)

        navigator.navigate({ "test" })

        verify(mockNavController).navigate("test")
    }

    @Test
    fun `navigate calls correct route on navController, true inclusive`() {
        navigator.setController(mockNavController)

        navigator.navigate({ "test" }, true)

        verify(mockNavController).navigate(route = eq("test"), builder = any())
    }

    @Test
    fun `navigating before setup does not work`() {
        navigator.navigate({ "test" })

        verifyNoInteractions(mockNavController)
    }

    @Test
    fun `reset removes the navController`() {
        val testRoute = NavRoute { "test" }
        navigator.setController(mockNavController)

        navigator.navigate(testRoute)
        navigator.reset()
        navigator.navigate(testRoute)

        verify(mockNavController, times(1))
            .navigate("test")
    }

    @Test
    fun `get back stack returns false for null back stack entry`() {
        whenever(mockNavController.previousBackStackEntry).thenReturn(null)
        navigator.setController(mockNavController)

        val result = navigator.hasBackStack()

        assertEquals(false, result)
    }

    @Test
    fun `get back stack returns true for existing back stack entry`() {
        val mockBackStackEntry: NavBackStackEntry = mock()
        whenever(mockNavController.previousBackStackEntry).thenReturn(mockBackStackEntry)

        navigator.setController(mockNavController)

        val result = navigator.hasBackStack()
        assertEquals(true, result)
    }

    @Test
    fun `popBackStack is called correctly`() {
        navigator.setController(mockNavController)
        navigator.goBack()

        verify(mockNavController).popBackStack()
    }
}
