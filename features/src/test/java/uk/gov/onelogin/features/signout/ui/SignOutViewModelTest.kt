package uk.gov.onelogin.features.signout.ui

import androidx.fragment.app.FragmentActivity
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.featureflags.FeatureFlags
import uk.gov.onelogin.core.navigation.data.ErrorRoutes
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.features.extensions.CoroutinesTestExtension
import uk.gov.onelogin.features.extensions.InstantExecutorExtension
import uk.gov.onelogin.features.featureflags.data.WalletFeatureFlag
import uk.gov.onelogin.features.signout.domain.SignOutError
import uk.gov.onelogin.features.signout.domain.SignOutUIState
import uk.gov.onelogin.features.signout.domain.SignOutUseCase

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class SignOutViewModelTest {
    private lateinit var viewModel: SignOutViewModel

    private val mockNavigator: Navigator = mock()
    private val mockActivity: FragmentActivity = mock()
    private val mockSignOutUseCase: SignOutUseCase = mock()
    private val featureFlags: FeatureFlags = mock()

    @BeforeEach
    fun setup() {
        viewModel =
            SignOutViewModel(
                mockNavigator,
                mockSignOutUseCase,
                featureFlags
            )
        whenever(featureFlags[WalletFeatureFlag.ENABLED]).then { true }
    }

    @Test
    fun `sign out use case does not throw`() =
        runTest {
            viewModel.signOut(mockActivity)

            verify(mockSignOutUseCase).invoke(mockActivity)
            verify(mockNavigator).navigate(LoginRoutes.Root, true)
        }

    @Test
    fun `sign out use case does throw`() =
        runTest {
            whenever(mockSignOutUseCase.invoke(mockActivity)).thenThrow(SignOutError(Exception()))

            viewModel.signOut(mockActivity)

            verify(mockSignOutUseCase).invoke(mockActivity)
            verify(mockNavigator).navigate(ErrorRoutes.SignOut, true)
        }

    @Test
    fun `goBack() correctly navigates back`() {
        viewModel.goBack()

        verify(mockNavigator).goBack()
    }

    @Test
    fun `uiState Wallet`() {
        whenever(featureFlags[WalletFeatureFlag.ENABLED]).then { true }
        val actual = viewModel.uiState
        assertEquals(expected = SignOutUIState.Wallet, actual = actual)
    }

    @Test
    fun `uiState No Wallet`() {
        whenever(featureFlags[WalletFeatureFlag.ENABLED]).then { false }
        val actual = viewModel.uiState
        assertEquals(expected = SignOutUIState.NoWallet, actual = actual)
    }
}
