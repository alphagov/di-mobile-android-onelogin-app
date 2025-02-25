package uk.gov.onelogin.features.optin.ui

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.features.extensions.CoroutinesTestExtension
import uk.gov.onelogin.features.extensions.InstantExecutorExtension
import uk.gov.onelogin.features.optin.data.AnalyticsOptInRepositoryTest.Companion.createTestAnalyticsOptInRepository

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class OptInViewModelTest {
    private val mockNavigator: Navigator = mock()
    private lateinit var viewModel: OptInViewModel

    @BeforeTest
    fun setUp() {
        viewModel =
            OptInViewModel(
                repository = createTestAnalyticsOptInRepository(),
                navigator = mockNavigator
            )
    }

    @Test
    fun `initial UI state is PreChoice`() =
        runTest {
            // Given a new OptInViewModel
            // Then the `uiState` is OptInUIState.PreChoice
            val state = viewModel.uiState.first()
            assertEquals(expected = OptInUIState.PreChoice, state)
        }

    @Test
    fun `optIn changes UI state to PostChoice`() =
        runTest {
            // Given a new OptInViewModel
            // When calling optIn()
            viewModel.optIn()
            // Then the `uiState` is OptInUIState.PostChoice
            val state = viewModel.uiState.first()
            assertEquals(expected = OptInUIState.PostChoice, state)
        }

    @Test
    fun `optOut changes UI state to PostChoice`() =
        runTest {
            // Given a new OptInViewModel
            // When calling optOut()
            viewModel.optOut()
            // Then the `uiState` is OptInUIState.PostChoice
            val state = viewModel.uiState.first()
            assertEquals(expected = OptInUIState.PostChoice, state)
        }

    @Test
    fun `navigate to sign`() {
        viewModel.goToSignIn()

        verify(mockNavigator).navigate(LoginRoutes.Welcome, true)
    }
}
