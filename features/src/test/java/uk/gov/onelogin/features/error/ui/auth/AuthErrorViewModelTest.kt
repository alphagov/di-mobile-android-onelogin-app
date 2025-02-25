package uk.gov.onelogin.features.error.ui.auth

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator

class AuthErrorViewModelTest {
    private val mockNavigator: Navigator = mock()
    private val sut = AuthErrorViewModel(mockNavigator)

    @Test
    fun `navigate to sign in`() =
        runTest {
            sut.navigateToSignIn()

            verify(mockNavigator).navigate(LoginRoutes.Start, popUpToInclusive = true)
        }
}
