package uk.gov.onelogin.features.signout.ui

import androidx.fragment.app.FragmentActivity
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.data.SignOutRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.domain.retrieve.GetPersistentId
import uk.gov.onelogin.core.tokens.domain.save.SaveTokens
import uk.gov.onelogin.features.extensions.CoroutinesTestExtension
import uk.gov.onelogin.features.extensions.InstantExecutorExtension
import uk.gov.onelogin.features.signout.domain.SignOutError
import uk.gov.onelogin.features.signout.domain.SignOutUseCase

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class SignedOutInfoViewModelTest {
    private val mockTokenRepository: TokenRepository = mock()
    private val mockSaveTokens: SaveTokens = mock()
    private val mockNavigator: Navigator = mock()
    private val mockGetPersistentId: GetPersistentId = mock()
    private val mockSignOutUseCase: SignOutUseCase = mock()

    private val viewModel by lazy {
        SignedOutInfoViewModel(
            mockNavigator,
            mockTokenRepository,
            mockSaveTokens,
            mockGetPersistentId,
            mockSignOutUseCase
        )
    }

    @Test
    fun `reset tokens calls use case`() {
        viewModel.resetTokens()

        verify(mockTokenRepository).clearTokenResponse()
    }

    @Test
    fun `save tokens calls use case`() =
        runTest {
            viewModel.saveTokens()

            verify(mockSaveTokens).invoke()
        }

    @Test
    fun `navigator has back stack, reauth is true`() {
        whenever(mockNavigator.hasBackStack()).thenReturn(true)
        assertTrue(viewModel.shouldReAuth())
    }

    @Test
    fun `navigator has no back stack, reauth is false`() {
        whenever(mockNavigator.hasBackStack()).thenReturn(false)
        assertFalse(viewModel.shouldReAuth())
    }

    @Test
    fun `sign out usecase is called when persistent id is null`() =
        runTest {
            var callback = false
            val activity: FragmentActivity = mock()
            whenever(mockGetPersistentId.invoke()).thenReturn(null)

            viewModel.checkPersistentId(activity) { callback = true }

            verify(mockSignOutUseCase).invoke(activity)
            verify(mockNavigator).navigate(SignOutRoutes.ReAuthError, true)
        }

    @Test
    fun `sign out usecase is called when persistent id is empty`() =
        runTest {
            var callback = false
            val activity: FragmentActivity = mock()
            whenever(mockGetPersistentId.invoke()).thenReturn("")

            viewModel.checkPersistentId(activity) { callback = true }

            verify(mockSignOutUseCase).invoke(activity)
            verify(mockNavigator).navigate(SignOutRoutes.ReAuthError, true)
        }

    @Test
    fun `sign out usecase is not called when persistent id good`() =
        runTest {
            var callback = false
            val activity: FragmentActivity = mock()
            whenever(mockGetPersistentId.invoke()).thenReturn("id")

            viewModel.checkPersistentId(activity) { callback = true }

            verifyNoInteractions(mockSignOutUseCase)
            verifyNoInteractions(mockNavigator)
            assertTrue(callback)
        }

    @Test
    fun `sign out usecase throws`() =
        runTest {
            var callback = false
            val activity: FragmentActivity = mock()
            whenever(mockGetPersistentId.invoke()).thenReturn("")
            whenever(mockSignOutUseCase.invoke(any())).thenThrow(SignOutError(Error()))

            viewModel.checkPersistentId(activity) { callback = true }

            verify(mockSignOutUseCase).invoke(activity)
            verify(mockNavigator).navigate(LoginRoutes.SignInError, true)
            assertFalse(callback)
        }
}
